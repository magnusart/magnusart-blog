import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

	val appName         = "Magnusart"
	val appVersion      = "1.0-SNAPSHOT"

    // Only compile the bootstrap bootstrap.less file and any other *.less file in the stylesheets directory 
    def customLessEntryPoints(base: File): PathFinder = ( 
    	(base / "app" / "assets" / "stylesheets" / "bootstrap" * "bootstrap.less") +++ 
    	(base / "app" / "assets" / "stylesheets" / "bootstrap" * "responsive.less") +++ 
    	(base / "app" / "assets" / "stylesheets" * "*.less") )


	val appDependencies = Seq(
		// Add your project dependencies here,
		"com.novus" %% "salat-core" % "0.0.8-SNAPSHOT" withSources,
    	"com.novus" %% "salat-util" % "0.0.8-SNAPSHOT" withSources,
		"org.scala-tools.time" % "time_2.9.1" % "0.5", // latest version at time of this writing
		"com.codahale" %% "jerkson" % "0.5.0",
        "com.edropple" % "velvetrope" % "1.0-SNAPSHOT",
        "org.jasypt" % "jasypt" % "1.9.0"
	)

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings( 
    	resolvers ++= Seq("Codahale" at "http://repo.codahale.com",
        "Ed Ropple's repository" at "http://maven.edropple.com",
        "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"),
        lessEntryPoints <<= baseDirectory(customLessEntryPoints)
        )

}
