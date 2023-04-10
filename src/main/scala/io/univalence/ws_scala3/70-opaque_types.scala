package io.univalence.ws_scala3

import java.util.UUID

import io.univalence.ws_scala3.internal.exercise_tools.*

/**
 * =Opaque types=
 * Among the many secondary features appearing in Scala 3, the ''opaque types''
 * is another especially interesting concepts which is worth talking about.
 * 
 * Opaque types is a feature that helps to implement domain-specific
 * models in a better way, it helps us get type abstractions without any
 * overhead.
 */
object _01_opaque_types {

  @main
  def _70_01_logarithm(): Unit =
    section("PART 1 - Logarithm example") {

      /**
       * Let us assume we want to define a module that offers arithmetic on
       * numbers, which are represented by their logarithm. Since it is
       * important to distinguish “regular” double values from numbers stored
       * as their logarithm, we introduce a type alias (for Double) Logarithm.
       */
      object Logarithms:
        /**
         * A `Logarithm` under the hood is just a `Double`. But this is only
         * known inside `Logarithms` for implementors.
         */
        opaque type Logarithm = Double

        object Logarithm:
          // We need a factory method to create one from a `Double`
          def apply(d: Double): Logarithm = math.log(d)

        // We want to enrich our type alias with some utility methods
        extension (x: Logarithm)
          def toDouble: Double = math.exp(x)
          def + (y: Logarithm): Logarithm = Logarithm(math.exp(x) + math.exp(y))
          def * (y: Logarithm): Logarithm = x + y

      exercise("Logarithm usage", activated = true) {
        import Logarithms.*

        /**
         * Outside `Logarithms`, the inner representation is not known (opaque).
         * The following lines will fail to compile if uncommented.
         */
        // val d: Double = log2 // ERROR: Found Logarithm required Double
        // val l: Logarithm = 3.0d // Found Double Required Logarithm

        // We need to use the factory to create a `Logarithm`
        val log2 = Logarithm(2.0)
        val log3 = Logarithm(3.0)
        
        // We can use enriched methods on `Logarithm`
        val multiple = log2 * log3
        val sum = log2 + log3

        // If we check the values
        check(multiple == ??)
        check(sum == ??)

        // We can convert it to Double
        check(multiple.toDouble == ??)
        check(sum.toDouble == ??)

        // We can benefit from equals from `Double`
        val areLogEquals = (Logarithm(5) + Logarithm(2)) == Logarithm(7)
        check(areLogEquals == ??)
      }

    }

  @main
  def _70_02_logarithm(): Unit =
    section("PART 2 - User domain modelization") {

      /**
       * We want to create a todo app where user could be logged in.
       * We want to modelize a `User` class with an `UUID` as an id and a
       * role. But we want our user id to be different from other `UUID`
       * which can be seen in our program.
       */ 
      case class User(id: User.UserId, name: String, role: User.UserRole)

      object User:
        /**
         * A `Logarithm` under the hood is just a `UUID`. 
         */
        opaque type UserId = UUID

        extension (uid: UserId)
          def getUUID: UUID = uid

        object UserId:
          // We need a factory method to create one from a `UUID`
          def apply(uuid: UUID): UserId = uuid

        /**
         * We prefer to modelize `UserRights` as `Int`. We will be able to use
         * native operator on integers to mix user rights. We do not provide
         * any factory method as we want our right modeling to be close (not
         * possible for an end user to extend it, could cause security issues).
         */
        opaque type UserRights = Int

        extension (r1: UserRights)
          def & (r2: UserRights): UserRights = r1 | r2

        object UserRights:
          val List: UserRights = 1
          val Edit: UserRights = 2
          val Delete: UserRights = 4

        /**
         * A user role will also be modelize as an Int. A role consists of a
         * list of user rights.
         */
        opaque type UserRole = UserRights

        extension (role: UserRole)
          // def is(required: UserRights) = (role & required) == required
          def contains(required: UserRights) = (role & required) != 0

        /**
         * We define standard roles for the application. Our modelization is
         * closed, our application will only support these roles for now.
         */
        object UserRole:
          val Visitor: UserRole = UserRights.List
          val Admin: UserRole = UserRights.List | UserRights.Edit | UserRights.Delete



      exercise("UserId dive in", activated = true) {
        import User.*

        // We create some uuid to create `UserId`
        val uuid1 = UUID.randomUUID()
        val uuid2 = UUID.randomUUID()

        // We want a `UserId` from uuid1
        val id1: UserId = |>?
        // We want a `UserId` from uuid2
        val id2: UserId = |>?

        // We can convert extracted values
        check(id1.getUUID == ??)
        check(id2.getUUID == ??)
      }

      exercise("UserRole dive in", activated = true) {
        import User.*

        // We can check if a role contains a user right
        check(UserRole.Visitor.contains(UserRights.List) == true)

        check(UserRole.Visitor.contains(UserRights.Edit) == false)

        check(UserRole.Admin.contains(UserRights.Edit) == true)

        // We can also check role if a role is similar to a set of rights
        val areRightsEquals = UserRole.Admin == (UserRights.List & UserRights.Edit & UserRights.Delete)

        check(areRightsEquals == true)
      }

    }

}
