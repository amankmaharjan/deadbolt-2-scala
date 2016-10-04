package be.objectify.deadbolt.scala.test.security

import be.objectify.deadbolt.scala.models.Subject
import be.objectify.deadbolt.scala.test.dao.SubjectDao
import be.objectify.deadbolt.scala.{AuthenticatedRequest, ConstraintPoint, DeadboltHandler, DynamicResourceHandler}
import play.api.Logger
import play.api.mvc.{Request, Result, Results}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
class MyDeadboltHandler(subjectDao: SubjectDao) extends DeadboltHandler {

  val logger: Logger = Logger(this.getClass)

  val dynamicHandler: Option[DynamicResourceHandler] = Some(new CompositeDynamicResourceHandler(Map("niceName" -> new NiceNameDynamicResourceHandler,
                                                                                                    "useMetaInfo" -> new UseMetaHintDynamicResourceHandler)))

  override def beforeAuthCheck[A](request: Request[A]): Future[Option[Result]] = Future(None)

  override def getDynamicResourceHandler[A](request: Request[A]): Future[Option[DynamicResourceHandler]] = Future.successful(dynamicHandler)

  override def getSubject[A](request: AuthenticatedRequest[A]): Future[Option[Subject]] =
    request.subject match {
      case Some(subject) => Future.successful(request.subject)
      case _ =>
        request.headers.get("x-deadbolt-test-user") match {
          case Some(userName) => Future {subjectDao.user(userName)}
          case None => Future.successful(None)
        }
    }

  override def onAuthFailure[A](request: AuthenticatedRequest[A]): Future[Result] = Future(Results.Unauthorized)

  override def onAuthSuccess[A](request: AuthenticatedRequest[A], constraintType: String, constraintPoint: ConstraintPoint): Unit =
    logger.info(s"[${request.subject.getOrElse("no user")}] granted access to [${request.path}] with [$constraintPoint] [$constraintType]")
}
