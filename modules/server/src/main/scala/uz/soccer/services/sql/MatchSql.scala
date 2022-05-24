package uz.soccer.services.sql

import skunk.{~, _}
import skunk.codec.all.timestamp
import skunk.implicits._
import uz.soccer.domain.Match
import uz.soccer.domain.Match.{CreateMatch, MatchWithUserName}
import uz.soccer.domain.types.MatchId
import uz.soccer.services.sql.StadiumSql.stadiumId
import uz.soccer.services.sql.UserSQL.userId

object MatchSql {
  val matchId: Codec[MatchId] = identity[MatchId]

  private val Columns = matchId ~ UserSQL.userId ~ StadiumSql.stadiumId ~ timestamp ~ timestamp
  private val ColumnsMatch = matchId ~ StadiumSql.stadiumId ~ userName ~ timestamp ~ timestamp

  val encoder: Encoder[MatchId ~ CreateMatch] =
    Columns.contramap { case i ~ m =>
      i ~ m.userId ~ m.stadiumId  ~ m.startTime ~ m.endTime
    }

  val decoder: Decoder[Match] =
    Columns.map { case i ~ ui ~ s ~ e ~ sid =>
      Match(i, ui, s, e, sid)
    }

  val decMatchWithUserName: Decoder[MatchWithUserName] =
    ColumnsMatch.map { case mi ~ si ~ un ~ st ~ et =>
      MatchWithUserName(mi, si, un, st, et)
    }

  val insert: Query[MatchId ~ CreateMatch, Match] =
    sql"""INSERT INTO matches VALUES ($encoder) returning *""".query(decoder)

  val selectAll: Query[Void, MatchWithUserName] =
    sql"""SELECT m.uuid, m.stadium_id, u.name, m.start_time, m.end_time FROM matches AS m
         INNER JOIN users u on u.uuid = m.uuid
       """.query(decMatchWithUserName)

  val update: Command[Match] =
    sql"""UPDATE matches SET
           user_id =$userId,
           start_time = $timestamp,
           end_time = $timestamp,
           stadium_id = ${StadiumSql.stadiumId} WHERE uuid = $matchId
       """.command.contramap(m => m.userId ~ m.startTime ~ m.endTime ~ m.stadiumId ~ m.uuid)

  val delete: Command[MatchId] =
    sql"""DELETE FROM matches WHERE uuid = $matchId""".command
}
