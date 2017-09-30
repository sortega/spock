organization := "org.refeed"

name := "spock"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.5" % "test",
  "org.scalacheck" %% "scalacheck" % "1.12.2" % "test",
  "org.scalaz" %% "scalaz-core" % "7.1.1"
)

