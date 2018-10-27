lazy val commonSettings = Seq(
  version      := "0.0.1",
  organization := "com.regblanc.clu",
  scalaVersion := "2.12.4",
  scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")
)

lazy val clu = (project in file("."))
  .settings(commonSettings: _*)
  .settings(
    name := "clu",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.0" % "test"
  )

lazy val example = (project in file("./example"))
  .settings(commonSettings: _*)
  .settings(
    name := "example"
  )
  .dependsOn(clu)
