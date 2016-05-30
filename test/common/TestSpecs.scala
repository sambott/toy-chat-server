package common

import java.io.File

import components.AppComponents
import org.scalatest.TestData
import org.scalatestplus.play.{PlaySpec, _}
import play.api
import play.api.ApplicationLoader.Context
import play.api.db.slick.DbName
import play.api.{ApplicationLoader, Environment, Mode}
import play.api.test._
import play.api.test.Helpers._
import slick.driver.JdbcProfile

/**
  * This file is subject to the terms and conditions defined in
  * file 'LICENSE.txt', which is part of this source code package.
  *
  * Sam Bott, 25/05/2016.
  */

object TestSpecs {

  class FakeApplicationComponents(context: Context) extends AppComponents(context) {

    protected override def dbConfig = api.dbConfig[JdbcProfile](DbName("testdb"))

  }

  class FakeAppLoader extends ApplicationLoader {
    override def load(context: Context): api.Application =
      new FakeApplicationComponents(context).application
  }

  def newApp: api.Application = {
    val appLoader = new FakeAppLoader
    val context = ApplicationLoader.createContext(
      new Environment(new File("."), ApplicationLoader.getClass.getClassLoader, Mode.Test)
    )
    appLoader.load(context)
  }

}

trait ApplicationSpec extends PlaySpec with OneAppPerTest {

  override def newAppForTest(testData: TestData): api.Application = TestSpecs.newApp

}


trait IntegrationSpec extends PlaySpec with OneServerPerTest with OneBrowserPerTest with HtmlUnitFactory {

  override def newAppForTest(testData: TestData): api.Application = TestSpecs.newApp

}
