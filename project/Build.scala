import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

	val appName         = "Magnusart"
	val appVersion      = "1.0-SNAPSHOT"

	val appDependencies = Seq(
      // Add your project dependencies here,
      )

    // Only compile the bootstrap bootstrap.less file and any other *.less file in the stylesheets directory 
    def customLessEntryPoints(base: File): PathFinder = ( 
    	(base / "app" / "assets" / "stylesheets" / "bootstrap" * "bootstrap.less") +++ 
    	(base / "app" / "assets" / "stylesheets" * "*.less") )


    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings( 
    	lessEntryPoints <<= baseDirectory(customLessEntryPoints) 
    	)

}
