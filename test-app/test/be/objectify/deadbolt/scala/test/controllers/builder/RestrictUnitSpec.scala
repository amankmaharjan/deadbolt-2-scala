package be.objectify.deadbolt.scala.test.controllers.builder

import be.objectify.deadbolt.scala.ActionBuilders
import be.objectify.deadbolt.scala.test.controllers.{AbstractRestrict, AbstractRestrictUnitSpec}
import play.api.inject.Injector
import play.api.mvc.ControllerComponents

/**
  * @author Steve Chaloner (steve@objectify.be)
  */
object RestrictUnitSpec extends AbstractRestrictUnitSpec {
  override def controller(injector: Injector): AbstractRestrict = new Restrict(injector.instanceOf[ActionBuilders], injector.instanceOf[ControllerComponents])
}
