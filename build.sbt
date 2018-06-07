name := "forex"
version := "1.0.0"

scalaVersion := "2.12.4"
scalacOptions ++= Seq(
  "-deprecation",
  "-encoding",
  "UTF-8",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-Ypartial-unification",
  "-language:experimental.macros",
  "-language:implicitConversions"
)

resolvers +=
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

val circeVersion = "0.9.3"
val effVersion = "5.1.0"

libraryDependencies ++= Seq(
  "com.github.pureconfig"          %% "pureconfig"           % "0.7.2",
  "com.softwaremill.quicklens"     %% "quicklens"            % "1.4.11",
  "com.typesafe.akka"              %% "akka-actor"           % "2.5.12",
  "com.typesafe.akka"              %% "akka-http"            % "10.1.1",
  "de.heikoseeberger"              %% "akka-http-circe"      % "1.21.0",
  "io.circe"                       %% "circe-core"           % circeVersion,
  "io.circe"                       %% "circe-generic"        % circeVersion,
  "io.circe"                       %% "circe-generic-extras" % circeVersion,
  "io.circe"                       %% "circe-java8"          % circeVersion,
  "io.circe"                       %% "circe-jawn"           % circeVersion,
  "io.circe"                       %% "circe-parser"         % circeVersion,
  "org.atnos"                      %% "eff"                  % effVersion,
  "org.atnos"                      %% "eff-monix"            % effVersion,
  "org.typelevel"                  %% "cats-core"            % "1.0.1",
  "org.zalando"                    %% "grafter"              % "2.3.0",
  "ch.qos.logback"                 %  "logback-classic"      % "1.2.3",
  "com.typesafe.scala-logging"     %% "scala-logging"        % "3.7.2",
  "org.scalatest"                  %% "scalatest"            % "3.0.5" % "test",
  compilerPlugin("org.spire-math"  %% "kind-projector"       % "0.9.4"),
  compilerPlugin("org.scalamacros" %% "paradise"             % "2.1.1" cross CrossVersion.full)
)
