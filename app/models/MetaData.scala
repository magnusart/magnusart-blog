package models
import org.scala_tools.time.Imports._
import models.MetaData._
import com.codahale.jerkson._

@JsonSnakeCase
case class Audit(updater:String, updated:DateTime = getLocalCurrentTime)
@JsonSnakeCase
case class MetaData(creator:String = "System", audit:List[Audit] = List.empty, created:DateTime = getLocalCurrentTime)

object MetaData {
	def getLocalCurrentTime = DateTime.now.withZone(DateTimeZone.forID("Europe/Stockholm"))

	val datePattern = "yyyy-MM-dd HH:mm"
	lazy val localDateTimeFormatter = DateTimeFormat.forPattern(datePattern).withZone(DateTimeZone.forID("Europe/Stockholm"))
}