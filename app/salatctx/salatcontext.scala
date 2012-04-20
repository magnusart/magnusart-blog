package salatctx

import com.novus.salat._
import play.api.Play.current
import play.api._

package object salatctx {
  /**
   * Here is where we define the custom Play serialization context, including the Play classloader.
   */
  implicit val ctx = {
    val c = new Context {
      val name = "play-context"
    }

    c.registerClassLoader(Play.classloader)

    c
  }

}