/*
 * Copyright 2018 com.github.gvolpe
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

package com.github.gvolpe.tracer.interpreter

import cats.MonadError
import com.github.gvolpe.tracer.Tracer.KFX
import com.github.gvolpe.tracer.TracerLog
import com.github.gvolpe.tracer.algebra.UserAlgebra
import com.github.gvolpe.tracer.model.user.{User, Username}
import com.github.gvolpe.tracer.program.UserProgram
import com.github.gvolpe.tracer.repository.algebra.UserRepository

class UserTracerInterpreter[F[_]](repo: UserRepository[KFX[F, ?]])(implicit F: MonadError[F, Throwable],
                                                                   L: TracerLog[KFX[F, ?]])
    extends UserProgram[KFX[F, ?]](repo) {

  override def find(username: Username): KFX[F, User] =
    for {
      _ <- L.info[UserAlgebra[F]](s"Find user by username: ${username.value}")
      u <- super.find(username)
    } yield u

  override def persist(user: User): KFX[F, Unit] =
    for {
      _  <- L.info[UserAlgebra[F]](s"About to persist user: ${user.username.value}")
      rs <- super.persist(user)
    } yield rs

}
