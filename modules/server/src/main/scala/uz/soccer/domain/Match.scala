package uz.soccer.domain

import derevo.cats.show
import derevo.circe.magnolia.{decoder, encoder}
import derevo.derive
import eu.timepit.refined.collection.NonEmpty
import eu.timepit.refined.types.string.NonEmptyString
import uz.soccer.domain.types.{MatchId, StadiumId, UserId, UserName}

import java.time.LocalDateTime

@derive(decoder, encoder, show)
case class Match(
  uuid: MatchId,
  userId: UserId,
  stadiumId: StadiumId,
  startTime: LocalDateTime,
  endTime: LocalDateTime
)

object Match {
  @derive(decoder, encoder, show)
  case class CreateMatch(
    userId: UserId,
    stadiumId: StadiumId,
    startTime: LocalDateTime,
    endTime: LocalDateTime
  )

  @derive(decoder, encoder, show)
  case class MatchWithUserName(
    uuid: MatchId,
    stadiumId: StadiumId,
    userName: UserName,
    startTime: LocalDateTime,
    endTime: LocalDateTime
  )
}
