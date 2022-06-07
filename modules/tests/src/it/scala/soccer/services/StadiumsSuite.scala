package soccer.services

import cats.effect.IO
import eu.timepit.refined.cats.refTypeShow
import uz.soccer.domain.Stadium
import uz.soccer.services.Stadiums
import uz.soccer.utils.DBSuite
import uz.soccer.utils.Generators.{createStadiumGen, priceGen, telGen}

object StadiumsSuite extends DBSuite {

  test("Create Stadium") { implicit postgres =>
    val stadiums = Stadiums[IO]
    forall(createStadiumGen) { createStadium =>
      for {
        stadium1 <- stadiums.create(createStadium)
        stadium2 <- stadiums.getAll
      } yield assert(stadium2.contains(stadium1))
    }
  }

    test("Update Stadium") { implicit postgres =>
    val stadiums = Stadiums[IO]
    val gen = for {
      c <- createStadiumGen
      t <- telGen
    } yield (c, t)
    forall(gen) { case (createStadium, tel) =>
      for {
        stadium1 <- stadiums.create(createStadium)
        stadium2 <- stadiums.update(
          Stadium(
            uuid = stadium1.uuid,
            address = stadium1.address,
            owner = stadium1.owner,
            tel = tel,
            price = stadium1.price
          )
        )
      } yield assert.same(stadium2.tel, tel)
    }
  }

  test("Delete Stadium") { implicit postgres =>
    val stadiums = Stadiums[IO]
    forall(createStadiumGen) { createStadium =>
      for {
        stadium1 <- stadiums.create(createStadium)
        _ <- stadiums.delete(stadium1.uuid)
        stadium2 <- stadiums.getAll
      } yield assert(!stadium2.contains(stadium1))
    }
  }

}
