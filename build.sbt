name := "fagdag-fruktsalat"

version := "1.0"

scalaVersion := "2.11.4"

val akkaVersion = "2.3.6"

resolvers += "spray repo" at "http://repo.spray.io"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-persistence-experimental" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
  "com.typesafe.akka" %% "akka-slf4j"   % akkaVersion,
  "com.typesafe.akka" %% "akka-contrib"   % akkaVersion,
  "org.scalatest"     %% "scalatest" % "2.2.1" % "test",
  "com.massrelevance" %% "dropwizard-scala" % "0.7.1",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.4.3",
  "io.spray"                     %% "spray-client" % "1.3.2",
  "io.spray"                     %% "spray-json" % "1.3.1"
)

libraryDependencies += "com.github.michaelpisula" %% "akka-persistence-inmemory" % "0.2.1" exclude("com.github.krasserm", "akka-persistence-testkit_2.11")
