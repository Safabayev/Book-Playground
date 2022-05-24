package uz.soccer.domain

import derevo.cats._
import derevo.circe.magnolia.{decoder, encoder}
import derevo.derive
import tsec.passwordhashers.PasswordHash
import tsec.passwordhashers.jca.SCrypt
import uz.soccer.domain.custom.refinements.{EmailAddress, Password, Tel}
import uz.soccer.domain.types._
import io.circe.refined._
import eu.timepit.refined.cats._

@derive(decoder, encoder, show)
case class User(id: UserId, name: UserName, email: EmailAddress, phone: Tel, role: Role)
object User {

  @derive(decoder, encoder, show)
  case class CreateUser(
    name: UserName,
    email: EmailAddress,
    phone: Tel,
    password: Password
  )

  @derive(decoder, encoder)
  case class UserWithPassword(user: User, password: PasswordHash[SCrypt])
}
