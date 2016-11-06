import ohnosequences.sbt.SbtS3Resolver._
import com.amazonaws.services.s3.model.Region
import sbt.Keys._
import sbt._
import sbtassembly.MergeStrategy

name := "Hazelcast-Client"

version := "0.1"

organization := "com.landoop"

val myScalaVersion = "2.11.8"
//val hazelcastVersion = 
val typesafeVersion = "1.3.0"
val scalaTest = "2.2.5"

val libraries = Seq(
  "org.scala-lang" % "scala-reflect" % myScalaVersion,
 // "com.hazelcast" % "hazelcast-all" %hazelcastVersion,
  "com.typesafe" % "config" % typesafeVersion,
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
      s3region := Region.EU_Ireland,
      s3overwrite := true,
      s3credentials := file(".s3credentials")
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