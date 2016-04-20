name := "chat_server"

version := "1.0"

lazy val `chat_server` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

val akkaVersion = "2.4.2"

libraryDependencies ++= Seq( evolutions, cache , ws, filters, specs2 % Test )

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
  "com.typesafe.play" %% "play-slick" % "2.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "2.0.0",
  "mysql" % "mysql-connector-java" % "5.1.38",
  "com.github.tototoshi" %% "slick-joda-mapper" % "2.1.0"
)

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
