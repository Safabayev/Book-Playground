package soccer.services

import cats.effect.IO
import eu.timepit.refined.cats.refTypeShow
import eu.timepit.refined.auto.autoUnwrap
import tsec.passwordhashers.jca.SCrypt
import uz.soccer.domain.{Match, Stadium}
import uz.soccer.services.{Matches, Stadiums, Users}
import uz.soccer.utils.DBSuite
import uz.soccer.utils.Generators.{createMatchGen, createStadiumGen, createUserGen, matchIdGen, stadiumGen, telGen, userGen, userIdGen, usernameGen}

object MatchesSuite extends DBSuite {

  test("Create Match") { implicit postgres =>
    val matches = Matches[IO]
    val users = Users[IO]
    val stadiums = Stadiums[IO]
    val gen = for {
      c <- createMatchGen
      u <- createUserGen
      s <- createStadiumGen
    } yield (c, u, s)
    forall(gen) { case (createMatch, newUser, newStadium) =>
      for {
        hash <- SCrypt.hashpw[IO](newUser.password)
        user1 <- users.create(newUser, hash)
        stadium <- stadiums.create(newStadium)
        match1 <- matches.create(createMatch.copy(userId = user1.id, stadiumId = stadium.uuid))
        match2 <- matches.getAll
      } yield assert(match2.contains(match1))
    }
  }

  test("Update Match") { implicit postgres =>
    val matches = Matches[IO]
    val users = Users[IO]
    val stadiums = Stadiums[IO]
    val gen = for {
      c <- createMatchGen
      u <- createUserGen
      s <- createStadiumGen
      ns <- stadiumGen
    } yield (c, u, s, ns)
    forall(gen) { case (createMatch, user, newStadium1, newStadium2) =>
      for {
        hash1 <- SCrypt.hashpw[IO](user.password)
        user1 <- users.create(user, hash1)
        stadium <- stadiums.create(newStadium1)
        match1 <- matches.create(createMatch.copy(userId = user1.id, stadiumId = stadium.uuid))
        _ <- matches.update(
          Match(
            uuid = match1.uuid,
            userId = match1.userId,
            stadiumId = newStadium2.uuid,
            startTime = match1.startTime,
            endTime = match1.endTime
          )
        )
        match2 <- matches.getAll.map(_.find(a => a.uuid == match1.uuid).get)
      } yield assert.same(match2.stadiumId, newStadium2.uuid)
    }
  }

  test("Delete Match") { implicit postgres =>
    val matches = Matches[IO]
    val users = Users[IO]
    val stadiums = Stadiums[IO]
    val gen = for {
      m <- createMatchGen
      u <- createUserGen
      s <- createStadiumGen
    } yield (m, u, s)
    forall(gen) { case (createMatch, createUser, createStadium) =>
      for {
        hash1 <- SCrypt.hashpw[IO](createUser.password)
        user1 <- users.create(createUser, hash1)
        stadium <- stadiums.create(createStadium)
        match1 <- matches.create(createMatch.copy(userId = user1.id, stadiumId = stadium.uuid))
        _ <- matches.delete(match1.uuid)
        match2 <- matches.getAll
      } yield assert(!match2.contains(match1))
    }
  }

}
