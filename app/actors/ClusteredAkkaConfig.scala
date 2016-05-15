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

  private val defaults = ConfigFactory.load().getConfig("clustered")
  private def env = System.getenv()
  private val port = defaults.getString("akka.remote.netty.tcp.port")

  val (host, bindHost, siblings) = env.getOrDefault(AUTOSCALE_VAR, "local").toUpperCase match {
    case "EC2" =>
      log info "Using EC2 autoscaling configuration"
      (ec2.currentIp, "::0", ec2.siblingIps)
    case _ =>
      log info "Running with local configuration"
      ("localhost", "::0" , "localhost" :: Nil)
  }

  val seeds = siblings map (ip => s"akka.tcp://clustered@$ip:$port")

  private val overrideConfig =
    ConfigFactory.empty()
      .withValue("akka.remote.netty.tcp.bind-hostname", ConfigValueFactory.fromAnyRef(bindHost))
      .withValue("akka.remote.netty.tcp.hostname", ConfigValueFactory.fromAnyRef(host))
      .withValue("akka.cluster.seed-nodes", ConfigValueFactory.fromIterable(seeds))

  val config = overrideConfig withFallback defaults
}
