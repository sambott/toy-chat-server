import com.typesafe.sbt.packager.docker._

name := "chat_server"
organization in ThisBuild := "bott.org.uk"
version := "1.4"

lazy val `chat_server` = (project in file(".")).enablePlugins(PlayScala,ClasspathJarPlugin,ElasticBeanstalkPlugin)

scalaVersion in ThisBuild := "2.11.8"
val akkaVersion = "2.4.2"

// Docker settings
maintainer in Docker := "Sam Bott"
dockerExposedPorts := Seq(9000)
dockerBaseImage := "java:latest"
// this chmods the file to executable, Windows doesn't set that bit in the zip file.
dockerCommands += ExecCmd("RUN",
  "chmod", "+x",
  s"${(defaultLinuxInstallLocation in Docker).value}/bin/${executableScriptName.value}")
// docker has no need for a running pid, it just gets in the way
javaOptions in Universal ++= Seq(
  "-Dpidfile.path=/dev/null"
)

// Dependencies
val angularVer = "1.4.9"
libraryDependencies ++= Seq(
  // Play components
  evolutions,
  filters,
  cache,
  ws,
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test,

  // Backend
  "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
  "com.typesafe.play" %% "play-slick" % "2.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "2.0.0",
  "mysql" % "mysql-connector-java" % "5.1.38",
  "com.h2database" % "h2" % "1.4.191" % Test,
  "com.github.tototoshi" %% "slick-joda-mapper" % "2.1.0",

  // WebJars (i.e. client-side) dependencies
  "org.webjars" % "requirejs" % "2.1.14-1",
  "org.webjars.bower" % "jquery" % "1.11.1",
  "org.webjars.bower" % "bootstrap" % "3.3.6",
  "org.webjars.bower" % "angular" % angularVer,
  "org.webjars.bower" % "angular-cookies" % angularVer,
  "org.webjars.bower" % "angular-route" % angularVer,
  "org.webjars.bower" % "angular-websocket" % "1.1.0"
)

// Scala Compiler Options
scalacOptions in ThisBuild ++= Seq(
  "-target:jvm-1.8",
  "-encoding", "UTF-8",
  "-deprecation", // warning and location for usages of deprecated APIs
  "-feature", // warning and location for usages of features that should be imported explicitly
  "-unchecked", // additional warnings where generated code depends on assumptions
  "-Xlint", // recommended additional warnings
  "-Xcheckinit", // runtime error when a val is not initialized due to trait hierarchies (instead of NPE somewhere else)
  "-Ywarn-adapted-args", // Warn if an argument list is modified to match the receiver
  "-Ywarn-value-discard", // Warn when non-Unit expression results are unused
  "-Ywarn-inaccessible",
  "-Ywarn-dead-code"
)

//
// sbt-web configuration
// https://github.com/sbt/sbt-web
//

// Configure the steps of the asset pipeline (used in stage and dist tasks)
// rjs = RequireJS, uglifies, shrinks to one file, replaces WebJars with CDN
// digest = Adds hash to filename
// gzip = Zips all assets, Asset controller serves them automatically when client accepts them
pipelineStages := Seq(rjs, digest, gzip)

// RequireJS with sbt-rjs (https://github.com/sbt/sbt-rjs#sbt-rjs)
// ~~~
RjsKeys.paths += ("jsRoutes" -> ("/jsroutes" -> "empty:"))

//RjsKeys.mainModule := "main"

// Asset hashing with sbt-digest (https://github.com/sbt/sbt-digest)
// ~~~
// md5 | sha1
//DigestKeys.algorithms := "md5"
//includeFilter in digest := "..."
//excludeFilter in digest := "..."

// HTTP compression with sbt-gzip (https://github.com/sbt/sbt-gzip)
// ~~~
// includeFilter in GzipKeys.compress := "*.html" || "*.css" || "*.js"
// excludeFilter in GzipKeys.compress := "..."

// JavaScript linting with sbt-jshint (https://github.com/sbt/sbt-jshint)
// ~~~
// JshintKeys.config := ".jshintrc"

// Add js tests
unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )

// All work and no play...
emojiLogs
