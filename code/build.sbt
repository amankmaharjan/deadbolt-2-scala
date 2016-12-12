name := "deadbolt-scala"

organization := "be.objectify"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  cache,
  specs2 % Test
)

parallelExecution in Test := false

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

resolvers += Resolver.sonatypeRepo("snapshots")

fork in Test := true

releasePublishArtifactsAction := PgpKeys.publishSigned.value
