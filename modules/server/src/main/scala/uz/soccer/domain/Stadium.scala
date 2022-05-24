package uz.soccer.domain

import derevo.cats._
import derevo.circe.magnolia.{decoder, encoder}
import derevo.derive
import uz.soccer.domain.custom.refinements.{EmailAddress, Tel}
import uz.soccer.domain.types.{Address, Owner, StadiumId}
import io.circe.refined._
import eu.timepit.refined.cats._
import squants.Money

import java.time.LocalDateTime

@derive(decoder, encoder, show)
case class Stadium(
  uuid: StadiumId,
  address: Address,
  owner: Owner,
  tel: Tel,
  price: Money)

object Stadium {
  @derive(decoder, encoder, show)
  case class CreateStadium(
    address: Address,
    owner: Owner,
    tel: Tel,
    price: Money)
}
