package uz.soccer.services.sql

import skunk._
import skunk.implicits._
import uz.soccer.domain.Stadium
import uz.soccer.domain.Stadium.{CreateStadium, UpdateStadium}
import uz.soccer.domain.types.StadiumId

object StadiumSql {
  val stadiumId: Codec[StadiumId] = identity[StadiumId]

  private val Columns = stadiumId ~ address ~ owner ~ tel ~ price

  val encoder: Encoder[StadiumId ~ CreateStadium] =
    Columns.contramap { case i ~ s =>
      i ~ s.address ~ s.owner ~ s.tel ~ s.price
    }
  val decoder: Decoder[Stadium] =
    Columns.map { case i ~ a ~ o ~ t ~ p =>
      Stadium(i, a, o, t, p)
    }

  val insert: Query[StadiumId ~ CreateStadium, Stadium] =
    sql"""INSERT INTO stadiums VALUES ($encoder) returning *""".query(decoder)

  val selectAll: Query[Void, Stadium] =
    sql"""SELECT * FROM stadiums""".query(decoder)

  val updateSql: Query[Stadium, Stadium] =
    sql"""UPDATE stadiums
       SET address = $address,
           owner = $owner,
           tel = $tel,
           price = $price
       WHERE uuid = $stadiumId RETURNING *"""
      .query(decoder)
      .contramap[Stadium](s => s.address ~ s.owner ~ s.tel ~ s.price ~ s.uuid)

  val delete: Command[StadiumId] =
    sql"""DELETE FROM stadiums WHERE uuid = $stadiumId""".command
}
