import play.api._

import models._
import com.mongodb.casbah.commons.conversions.scala._ 
import org.scala_tools.time.Imports._

object Global extends GlobalSettings {
  override def onStart(app: Application) {
	RegisterJodaTimeConversionHelpers() 

  }
}
