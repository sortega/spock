organization := "org.refeed"

name := "spock"

version := "0.1-SNAPSHOT"

scalaVersion := "3.3.6"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.19" % Test,
  "org.scalatestplus" %% "scalacheck-1-18" % "3.2.19.0" % Test
)

assembly / mainClass := Some("spock.Main")

// Prepend a launcher script to the fat jar so the output is a self-executing
// `spock` binary (still needs a JVM on the PATH).
assembly / assemblyPrependShellScript :=
  Some(sbtassembly.AssemblyPlugin.defaultUniversalScript(shebang = true))

assembly / assemblyJarName := "spock"

assembly / assemblyOutputPath := baseDirectory.value / "spock"
