organization := "org.refeed"

name := "spock"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.1.4" % "test",
  "org.scalacheck" %% "scalacheck" % "1.12.0" % "test",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.9",
  "org.scalaz" %% "scalaz-core" % "7.1.1"
)

