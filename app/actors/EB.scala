package actors

import java.io.{BufferedReader, InputStreamReader}
import java.net.URL

import com.amazonaws.services.autoscaling.AmazonAutoScalingClient
import com.amazonaws.services.ec2.AmazonEC2Client
import com.amazonaws.services.ec2.model.{DescribeInstancesRequest, Filter, Instance}

import scala.collection.JavaConversions._
import scala.language.postfixOps


/**
  * This file is subject to the terms and conditions defined in
  * file 'LICENSE.txt', which is part of this source code package.
  *
  * Sam Bott, 09/05/2016.
  */

class EB(scaling: AmazonAutoScalingClient, ec2: AmazonEC2Client) {

  def siblingIps: List[String] = peers map (_.getPrivateIpAddress) toList

  def currentIp = instanceFromId(instanceId).getPrivateIpAddress

  private def instanceId = {
    val conn = new URL("http://169.254.169.254/latest/meta-data/instance-id").openConnection
    val in = new BufferedReader(new InputStreamReader(conn.getInputStream))
    try in.readLine() finally in.close()
  }

  private def instanceFromId(id: String): Instance = {
    val result = ec2 describeInstances new DescribeInstancesRequest {
      setInstanceIds(id :: Nil)
    }
    result.getReservations.head.getInstances.head
  }

  private def environmentName = {
    val filter = new Filter("instance-state-name").withValues("running")
    for {
      instance <- instancesFromFilter(Seq(filter))
      tag <- instance.getTags
      if "elasticbeanstalk:environment-name" == tag.getKey
    } yield tag.getValue
  }

  private def peers: Seq[Instance] = {
    val filters = Seq(
      new Filter("instance-state-name").withValues("running"),
      new Filter("tag:elasticbeanstalk:environment-name=" + environmentName)
    )
    instancesFromFilter(filters)
  }

  private def instancesFromFilter(filters: Seq[Filter]) = {
    val request = new DescribeInstancesRequest().withInstanceIds(instanceId).withFilters(filters)
    for {
      reservation <- ec2.describeInstances(request).getReservations
      instance <- reservation.getInstances
    } yield instance
  }

}
