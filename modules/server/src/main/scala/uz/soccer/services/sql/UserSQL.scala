package uz.soccer.services.sql

import skunk._
import skunk.implicits._
import tsec.passwordhashers.PasswordHash
import tsec.passwordhashers.jca.SCrypt
import uz.soccer.domain.User.CreateUser
import uz.soccer.domain.custom.refinements.EmailAddress
import uz.soccer.domain.types._
import uz.soccer.domain.{Role, User}

object UserSQL {
  val userId: Codec[UserId] = identity[UserId]

  private val Columns = userId ~ userName ~ email ~ tel ~ passwordHash ~ role

  val encoder: Encoder[UserId ~ CreateUser ~ PasswordHash[SCrypt]] =
    Columns.contramap { case i ~ u ~ p =>
      i ~ u.name ~ u.email ~ u.phone ~ p ~ Role.USER
    }
  val decoder: Decoder[User ~ PasswordHash[SCrypt]] =
    Columns.map { case i ~ n ~ e ~ g ~ p ~ r =>
      User(i, n, e, g, r) ~ p
    }

  val selectUser: Query[EmailAddress, User ~ PasswordHash[SCrypt]] =
    sql"""SELECT * FROM users WHERE email = $email""".query(decoder)

  val insertUser: Query[UserId ~ CreateUser ~ PasswordHash[SCrypt], User ~ PasswordHash[SCrypt]] =
    sql"""INSERT INTO users VALUES ($encoder) returning *""".query(decoder)

}
