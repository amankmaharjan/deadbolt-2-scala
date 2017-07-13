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

import java.util.regex.Pattern
import javax.inject.Singleton

import scala.collection.mutable

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
@Singleton
class DefaultPatternCache extends PatternCache {

  private val cache: mutable.Map[String, Option[Pattern]] = mutable.Map[String, Option[Pattern]]()

  override def apply(value: String): Option[Pattern] = cache.getOrElse[Option[Pattern]](value, Some(Pattern.compile(value)))
}
