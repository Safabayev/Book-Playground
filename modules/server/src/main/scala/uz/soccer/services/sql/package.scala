package uz.soccer.services

import eu.timepit.refined.types.string.NonEmptyString
import skunk.Codec
import skunk.codec.all._
import skunk.data.{Arr, Type}
import uz.soccer.domain.custom.refinements.{EmailAddress, Tel}
import uz.soccer.domain.types.{Address, Owner, TeamName, UZS, UserName}
import uz.soccer.domain.Role
import uz.soccer.types.IsUUID
import eu.timepit.refined.auto.autoUnwrap
import squants.Money
import tsec.passwordhashers.PasswordHash
import tsec.passwordhashers.jca.SCrypt

import java.util.UUID
import scala.util.Try

package object sql {

  def parseUUID: String => Either[String, UUID] = s =>
    Try(Right(UUID.fromString(s))).getOrElse(Left(s"Invalid argument: [ $s ]"))

  val _uuid: Codec[Arr[UUID]] = Codec.array(_.toString, parseUUID, Type._uuid)

  val listUUID: Codec[List[UUID]] = _uuid.imap(_.flattenTo(List))(l => Arr(l: _*))

  def identity[A: IsUUID]: Codec[A] = uuid.imap[A](IsUUID[A]._UUID.get)(IsUUID[A]._UUID.apply)

  val userName: Codec[UserName] = varchar.imap[UserName](name => UserName(NonEmptyString.unsafeFrom(name)))(_.value)

  val teamName: Codec[TeamName] = varchar.imap[TeamName](name => TeamName(NonEmptyString.unsafeFrom(name)))(_.value)

  val passwordHash: Codec[PasswordHash[SCrypt]] = varchar.imap[PasswordHash[SCrypt]](PasswordHash[SCrypt])(_.toString)

  val email: Codec[EmailAddress] = varchar.imap[EmailAddress](EmailAddress.unsafeFrom)(_.value)

  val tel: Codec[Tel] = varchar.imap[Tel](Tel.unsafeFrom)(_.value)

  val price: Codec[Money] = numeric.imap[Money](money => UZS(money))(_.amount)

  val address: Codec[Address] = varchar.imap[Address](str => Address(NonEmptyString.unsafeFrom(str)))(_.value)

  val owner: Codec[Owner] = varchar.imap[Owner](str => Owner(NonEmptyString.unsafeFrom(str)))(_.value)

  val role: Codec[Role] = `enum`[Role](_.value, Role.find, Type("role"))

}
