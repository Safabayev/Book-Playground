package uz.soccer.utils

import eu.timepit.refined.scalacheck.string._
import eu.timepit.refined.types.string.NonEmptyString
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import squants.Money
import uz.soccer.domain.Match.CreateMatch
import uz.soccer.domain.Stadium.CreateStadium
import uz.soccer.domain.Team.CreateTeam
import uz.soccer.domain.User._
import uz.soccer.domain.custom.refinements.{EmailAddress, FileName, Password, Tel}
import uz.soccer.domain.types._
import uz.soccer.domain.{Credentials, Match, Role, Stadium, Team, User}
import uz.soccer.utils.Arbitraries._

import java.time.LocalDateTime
import java.util.UUID

object Generators {

  def nonEmptyStringGen(min: Int, max: Int): Gen[String] =
    Gen
      .chooseNum(min, max)
      .flatMap { n =>
        Gen.buildableOfN[String, Char](n, Gen.alphaChar)
      }

  def nonEmptyAlphaNumGen(min: Int, max: Int): Gen[String] =
    Gen
      .chooseNum(min, max)
      .flatMap { n =>
        Gen.buildableOfN[String, Char](n, Gen.alphaNumChar)
      }

  def numberGen(length: Int): Gen[String] = Gen.buildableOfN[String, Char](length, Gen.numChar)

  def idGen[A](f: UUID => A): Gen[A] =
    Gen.uuid.map(f)

  val userIdGen: Gen[UserId] =
    idGen(UserId.apply)

  val teamIdGen: Gen[TeamId] =
    idGen(TeamId.apply)

  val stadiumIdGen: Gen[StadiumId] =
    idGen(StadiumId.apply)

  val matchIdGen: Gen[MatchId] =
    idGen(MatchId.apply)

  val usernameGen: Gen[UserName] =
    arbitrary[NonEmptyString].map(UserName.apply)

  val addressGen: Gen[Address] =
    arbitrary[NonEmptyString].map(Address.apply)

  val ownerGen: Gen[Owner] =
    arbitrary[NonEmptyString].map(Owner.apply)

  val teamNameGen: Gen[TeamName] = arbitrary[NonEmptyString].map(TeamName.apply)

  val createTeamGen: Gen[CreateTeam] = teamNameGen.map(CreateTeam.apply)

  val passwordGen: Gen[Password] = arbitrary[Password]

  val dateTimeGen: Gen[LocalDateTime] = arbitrary[LocalDateTime]

  val telGen: Gen[Tel] = arbitrary[Tel]

  val priceGen: Gen[Money] = Gen.posNum[Long].map(n => UZS(BigDecimal(n)))

  val booleanGen: Gen[Boolean] = arbitrary[Boolean]

  val emailGen: Gen[EmailAddress] = arbitrary[EmailAddress]

  val filenameGen: Gen[FileName] = arbitrary[FileName]

  val roleGen: Gen[Role] = arbitrary[Role]

  val userGen: Gen[User] =
    for {
      i <- userIdGen
      n <- usernameGen
      e <- emailGen
      t <- telGen
      r <- roleGen
    } yield User(i, n, e, t, r)

  val userCredentialGen: Gen[Credentials] =
    for {
      e <- emailGen
      p <- passwordGen
    } yield Credentials(e, p)

  val teamGen: Gen[Team] =
    for {
      i <- teamIdGen
      n <- teamNameGen
    } yield Team(i, n)

  val createUserGen: Gen[CreateUser] =
    for {
      u <- usernameGen
      e <- emailGen
      t <- telGen
      p <- passwordGen
    } yield CreateUser(u, e, t, p)

  val createStadiumGen: Gen[CreateStadium] =
    for {
      a <- addressGen
      o <- ownerGen
      t <- telGen
      p <- priceGen
    } yield CreateStadium(a, o, t, p)

  val stadiumGen: Gen[Stadium] =
    for {
      i <- stadiumIdGen
      a <- addressGen
      o <- ownerGen
      t <- telGen
      p <- priceGen
    } yield Stadium(i, a, o, t, p)

  val createMatchGen: Gen[CreateMatch] =
    for {
      ui <- userIdGen
      s  <- stadiumIdGen
      st <- dateTimeGen
      et <- dateTimeGen
    } yield CreateMatch(ui, s, st, et)

  val matchGen: Gen[Match] =
    for {
      ud <- matchIdGen
      i  <- userIdGen
      s  <- stadiumIdGen
      st <- dateTimeGen
      et <- dateTimeGen
    } yield Match(ud, i, s, st, et)
}
