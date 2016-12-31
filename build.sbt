import sbt.Keys._
import sbt._
import sbtassembly.MergeStrategy

name := "Hazelcast-Client"

version := "0.1"

organization := "com.landoop"

val myScalaVersion = "2.11.8"
val typesafeVersion = "1.3.0"
val hazelcastVersion = "3.7.2"
val scalaTest = "2.2.5"

val libraries = Seq(
  "org.scala-lang" % "scala-reflect" % myScalaVersion,
  "com.hazelcast" % "hazelcast-all" % hazelcastVersion,
  "com.hazelcast" %% "hazelcast-scala" % hazelcastVersion withSources(),
  "com.typesafe" % "config" % typesafeVersion,
  "log4j" % "log4j" % "1.2.17",
  "org.scalatest" %% "scalatest" % scalaTest % "test, it"
)

lazy val root =
  Project("root", file("."))
    .configs(IntegrationTest)
    .settings(Defaults.itSettings: _*)
    .settings(
      name := "hazelcast-client",
      version := "1.1-SNAPSHOT",
      scalaVersion := myScalaVersion,
      resolvers ++= Seq(
        Resolver.mavenLocal,
        Resolver.bintrayRepo("hseeberger", "maven"),
        Resolver.bintrayRepo("nilskp", "maven"),
        "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
        "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
      ),
      libraryDependencies ++= libraries,
      assemblyMergeStrategy in assembly := {
        case x if x.startsWith("META-INF") && !x.contains("jackson") => MergeStrategy.discard
        case x if x.contains("jackson") => MergeStrategy.first
        case x => MergeStrategy.first
      },
      scalacOptions ++= Seq("-target:jvm-1.7", "-feature"),
      parallelExecution in Test := false,
      parallelExecution in IntegrationTest := false
    )
    .settings(
      test in assembly := {},
      mainClass in Compile := Some("com.landoop.hazelcast.Main"),
      assemblyJarName in assembly := "hazelcast-client.jar",
      artifact in(Compile, assembly) := {
        val art = (artifact in(Compile, assembly)).value
        art.copy(`classifier` = Some("assembly"))
      },
      addArtifact(artifact in(Compile, assembly), assembly)
    )
