package actors

/**
  * This file is subject to the terms and conditions defined in
  * file 'LICENSE.txt', which is part of this source code package.
  *
  * Sam Bott, 09/05/2016.
  */
import com.amazonaws.auth.InstanceProfileCredentialsProvider
import com.amazonaws.regions.{Regions, Region}
import com.amazonaws.services.autoscaling.AmazonAutoScalingClient
import com.amazonaws.services.ec2.AmazonEC2Client
import com.typesafe.config.{ConfigValueFactory, ConfigFactory}
import scala.collection.JavaConversions._

object ClusteredAkkaConfig {

  private lazy val ec2 = {
    val credentials = new InstanceProfileCredentialsProvider
    val region = Region.getRegion(Regions.EU_WEST_1)
    val scalingClient = new AmazonAutoScalingClient(credentials) { setRegion(region) }
    val ec2Client = new AmazonEC2Client(credentials) { setRegion(region) }
    new EC2(scalingClient, ec2Client)
  }

  private val local = sys.props.get("clustered.akka.port").isDefined

  val (host, siblings, port) =
    if (local) {
      println("Running with local configuration")
      ("localhost", "localhost" :: Nil, sys.props("clustered.akka.port"))
    } else {
      println("Using EC2 autoscaling configuration")
      (ec2.currentIp, ec2.siblingIps, "2551")
    }

  val seeds = siblings map (ip => s"akka.tcp://akka-ec2@$ip:2551")

  private val overrideConfig =
    ConfigFactory.empty()
      .withValue("akka.remote.netty.tcp.hostname", ConfigValueFactory.fromAnyRef(host))
      .withValue("akka.remote.netty.tcp.port", ConfigValueFactory.fromAnyRef(port))
      .withValue("akka.cluster.seed-nodes", ConfigValueFactory.fromIterable(seeds))

  private val defaults = ConfigFactory.load().getConfig("clustered")

  val config = overrideConfig withFallback defaults
}
