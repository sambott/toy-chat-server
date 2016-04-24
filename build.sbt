import com.typesafe.sbt.packager.docker._

name := "chat_server"

version := "1.0"

lazy val `chat_server` = (project in file(".")).enablePlugins(PlayScala,ElasticBeanstalkPlugin)

scalaVersion := "2.11.7"

val akkaVersion = "2.4.2"

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"


//docker settings
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


//experimental faster building
incOptions := incOptions.value.withNameHashing(true)
updateOptions := updateOptions.value.withCachedResolution(cachedResoluton = true)

libraryDependencies ++= Seq( evolutions, cache , ws, filters, specs2 % Test )

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
  "com.typesafe.play" %% "play-slick" % "2.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "2.0.0",
  "mysql" % "mysql-connector-java" % "5.1.38",
  "com.github.tototoshi" %% "slick-joda-mapper" % "2.1.0",


  //Web Jars

  //angular2 dependencies
  "org.webjars.npm" % "angular2" % "2.0.0-beta.15",
  "org.webjars.npm" % "systemjs" % "0.19.26",
  "org.webjars.npm" % "todomvc-common" % "1.0.2",
  "org.webjars.npm" % "rxjs" % "5.0.0-beta.2",
  "org.webjars.npm" % "es6-promise" % "3.0.2",
  "org.webjars.npm" % "es6-shim" % "0.35.0",
  "org.webjars.npm" % "reflect-metadata" % "0.1.2",
  "org.webjars.npm" % "zone.js" % "0.6.10",
  "org.webjars.npm" % "typescript" % "1.8.10",

  //tslint dependency
  "org.webjars.npm" % "tslint-eslint-rules" % "1.0.1"
)
dependencyOverrides += "org.webjars.npm" % "minimatch" % "2.0.10"

// the typescript typing information is by convention in the typings directory
// It provides ES6 implementations. This is required when compiling to ES5.
typingsFile := Some(baseDirectory.value / "typings" / "browser.d.ts")

// use the webjars npm directory (target/web/node_modules ) for resolution of module imports of angular2/core etc
resolveFromWebjarsNodeModulesDir := true

//if we use a module that has no typings info, uncomment below
//tsCodesToIgnore := List(canNotFindModule)

// use the combined tslint and eslint rules
(rulesDirectories in tslint) := Some(List((tslintEslintRulesDir).value))
