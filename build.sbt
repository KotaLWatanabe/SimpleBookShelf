name := """bookshelf"""
organization := "bookshelf"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.10"

val catsVersion = "2.9.0"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % Test

libraryDependencies += "org.typelevel" %% "cats-core" % catsVersion

libraryDependencies ++= Seq(
  "com.softwaremill.sttp.client3" %% "core" % "3.8.11"
)

// https://mvnrepository.com/artifact/software.amazon.awssdk/dynamodb
libraryDependencies += "software.amazon.awssdk" % "dynamodb" % "2.20.6"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "bookshelf.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "bookshelf.binders._"
