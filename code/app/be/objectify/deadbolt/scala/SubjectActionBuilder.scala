package be.objectify.deadbolt.scala

import be.objectify.deadbolt.core.models.Subject
import play.api.mvc.{Request, ActionTransformer, ActionBuilder}

import scala.concurrent.Future

/**
  * @author Steve Chaloner (steve@objectify.be)
  */
object SubjectActionBuilder {

  def apply(subject: Option[Subject]) = AuthenticatedActionBuilder(subject)

  case class AuthenticatedActionBuilder(subject: Option[Subject]) extends SubjectActionBuilder
}

trait SubjectActionBuilder extends ActionBuilder[AuthenticatedRequest] with ActionTransformer[Request, AuthenticatedRequest] {

  def subject: Option[Subject]

  def transform[A](request: Request[A]) = Future.successful{
    AuthenticatedRequest(request, subject)
                                                           }
}