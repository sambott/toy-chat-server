package actors

/**
  * This file is subject to the terms and conditions defined in
  * file 'LICENSE.txt', which is part of this source code package.
  *
  * Sam Bott, 09/05/2016.
  */
import com.amazonaws.auth.InstanceProfileCredentialsProvider
import com.amazonaws.regions.{Region, Regions}
import com.amazonaws.services.autoscaling.AmazonAutoScalingClient
import com.amazonaws.services.ec2.AmazonEC2Client
import com.typesafe.config.{ConfigFactory, ConfigValueFactory}
import play.api.Logger

import scala.collection.JavaConversions._

object ClusteredAkkaConfig {

  private val log = Logger("ClusterConfig")
  private val AUTOSCALE_VAR = "AUTOSCALE"

  private lazy val ec2 = {
    val credentials = new InstanceProfileCredentialsProvider
    val region = Region.getRegion(Regions.EU_WEST_1)
    val scalingClient = new AmazonAutoScalingClient(credentials) { setRegion(region) }
    val ec2Client = new AmazonEC2Client(credentials) { setRegion(region) }
    new EC2(scalingClient, ec2Client)
  }

  private lazy val eb = {
    val credentials = new InstanceProfileCredentialsProvider
    val region = Regions.getCurrentRegion
    val scalingClient = new AmazonAutoScalingClient(credentials) { setRegion(region) }
    val ebClient = new AmazonEC2Client(credentials) { setRegion(region) }
    new EB(scalingClient, ebClient)
  }

  private val defaults = ConfigFactory.load().getConfig("clustered")
  private def env = System.getenv()

  val (host, siblings, port) = env.getOrDefault(AUTOSCALE_VAR, "local").toUpperCase match {
    case "EB" =>
      log info "Using EB autoscaling configuration"
      (eb.currentIp, eb.siblingIps, "2551")
    case "EC2" =>
      log info "Using EC2 autoscaling configuration"
      (ec2.currentIp, "localhost" :: ec2.siblingIps, "2551")
    case _ =>
      log info "Running with local configuration"
      ("localhost", "localhost" :: Nil, defaults.getString("akka.remote.netty.tcp.port"))
  }

  val seeds = siblings map (ip => s"akka.tcp://clustered@$ip:2551")

  private val overrideConfig =
    ConfigFactory.empty()
      .withValue("akka.remote.netty.tcp.hostname", ConfigValueFactory.fromAnyRef(host))
      .withValue("akka.remote.netty.tcp.port", ConfigValueFactory.fromAnyRef(port))
      .withValue("akka.cluster.seed-nodes", ConfigValueFactory.fromIterable(seeds))

  val config = overrideConfig withFallback defaults
}
