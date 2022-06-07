package soccer.services

import cats.effect.IO
import eu.timepit.refined.cats.refTypeShow
import tsec.passwordhashers.jca.SCrypt
import eu.timepit.refined.auto.autoUnwrap
import uz.soccer.services.Users
import uz.soccer.utils.DBSuite
import uz.soccer.utils.Generators.createUserGen

object UsersSuite extends DBSuite {

  test("Create User") { implicit postgres =>
    val users = Users[IO]
    forall(createUserGen) { createUser =>
      SCrypt.hashpw[IO](createUser.password).flatMap { hash =>
        for {
          user1 <- users.create(createUser, hash)
          user2 <- users.find(user1.email)
        } yield assert(user2.exists(_.user == user1))
      }
    }
  }

}
