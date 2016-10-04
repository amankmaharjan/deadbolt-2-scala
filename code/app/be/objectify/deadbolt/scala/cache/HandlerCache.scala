/*
 * Copyright 2012-2016 Steve Chaloner
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.objectify.deadbolt.scala.cache

import be.objectify.deadbolt.scala.models.Subject
import be.objectify.deadbolt.scala.{AuthenticatedRequest, HandlerKey, DynamicResourceHandler, DeadboltHandler}
import play.api.mvc.{Result, Request}

import scala.concurrent.Future

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
trait HandlerCache extends Function[HandlerKey, DeadboltHandler] with Function0[DeadboltHandler] {
  val defaultHandlerName: String = "defaultHandler"


  /**
   * Wraps the default handler in another handler that caches the result of getSubject.  Use this on a per-request basis.
   *
   * @return a handler
   */
  @deprecated(message = "Use the subject accessible via AuthenticatedRequest instead.  This will be removed in Deadbolt 2.6.", since = "2.5.1")
  def withCaching = new SubjectCachingHandler(apply())

  /**
   * Wraps the handler in another handler that caches the result of getSubject.  Use this on a per-request basis.
   *
   * @return a handler
   */
  @deprecated(message = "Use the subject accessible via AuthenticatedRequest instead.  This will be removed in Deadbolt 2.6.", since = "2.5.1")
  def withCaching(handlerKey: HandlerKey): DeadboltHandler = new SubjectCachingHandler(apply(handlerKey))

  @deprecated(message = "Use the subject accessible via AuthenticatedRequest instead.  This will be removed in Deadbolt 2.6.", since = "2.5.1")
  class SubjectCachingHandler(delegate: DeadboltHandler) extends DeadboltHandler {

    override def beforeAuthCheck[A](request: Request[A]): Future[Option[Result]] = delegate.beforeAuthCheck(request)

    override def getSubject[A](request: AuthenticatedRequest[A]): Future[Option[Subject]] =
      request.subject match {
        case Some(subject) => Future.successful(request.subject)
        case _ => delegate.getSubject(request)
      }

    override def onAuthFailure[A](request: AuthenticatedRequest[A]): Future[Result] = delegate.onAuthFailure(request)

    override def getDynamicResourceHandler[A](request: Request[A]): Future[Option[DynamicResourceHandler]] = delegate.getDynamicResourceHandler(request)
  }
}
