package io.univalence.ws_scala3

import io.univalence.ws_scala3.internal.exercise_tools._

/**
 * =Contextual abstraction=
 *
 * The Scala 2 implicits era is gone. All the implicits use cases has
 * been rethought and new words like summon, using, extension or given
 * are here to replace the usual implicit def, implicit val and implicit
 * class.
 *
 * The implicits still exist but we should use the new words.
 */

object _01_given {
  trait Combinator[A] {
    def combine(lhs: A, rhs: A): A
  }

  /**
   * We can implicit give an instance of our type class using given
   * keyword.
   */
  given Combinator[Int] with {
    override def combine(lhs: Int, rhs: Int): Int = lhs + rhs
  }

  /**
   * We also have syntaxic way of declaring an instance of our type
   * class composed by only one function.
   */
  given Combinator[String] = (lhs: String, rhs: String) => lhs + rhs

  /** Note that you can give a name to an instance. */
  given boolCombinator: Combinator[Boolean] with {
    override def combine(lhs: Boolean, rhs: Boolean): Boolean = lhs || rhs
  }

  @main
  def _01_given_and_summon(): Unit =
    section("PART 1 - given and summon to define and get contextual abstraction") {
      exercise("Combine 2 Ints", activated = true) {

        /**
         * As our instances of Combinator are anonymous, we cannot get
         * them by their names, as we used to do when we have defined a
         * variable. Here, we will rather use `summon`.
         *
         * Info: in Scala 2, it was named `implicitly`.
         */
        check(summon[Combinator[Int]].combine(1, 2) == ??)

        /** We can style use a name if we want to. */
        check(boolCombinator.combine(true, false) == ??)
      }

      exercise("Combine 2 Strings", activated = false) {
        val v1 = "Hello"
        val v2 = "World"

        /**
         * Use summon here to get the right instance of Combinator to
         * combine v1 and v2.
         */
        val result: String = ???

        check(result == "HelloWorld")
      }
    }

}

object _02_using {
  trait Combinator[A] {
    def combine(lhs: A, rhs: A): A
  }

  given intCombinator: Combinator[Int] with {
    override def combine(lhs: Int, rhs: Int): Int = lhs + rhs
  }

  @main
  def _01_using_in_function(): Unit =
    section("PART 2 - ") {
      exercise("using with named context parameter", activated = true) {

        /**
         * The `using` keyword helps to declare a context parameter,
         * meaning an optional parameter, whose value may come from the
         * current or the outside scope.
         */
        def fold[A](init: A)(l: List[A])(using combinator: Combinator[A]): A = l.fold(init)(combinator.combine)

        /**
         * You can explicitly indicate the context parameter. In this
         * case, you must write `using` first.
         */
        check(fold(0)(List(1, 2, 3))(using intCombinator) == ??)

        /**
         * If you do not write the context parameter, Scala will look up
         * for an instance in the current and outside scope that
         * satisfies the context parameter type at the call of the
         * function.
         */
        check(fold(0)(List(1, 2, 3)) == ??)

        /**
         * When you explicitly provide a context parameter, you have to
         * write the keyword `using`.
         *
         * The line below produces a compilation error.
         */
        // fold(0)(List(1, 2, 3))(intCombinator)
      }

      exercise("using with anonymous context parameter", activated = true) {

        /**
         * You don't have to specify a name for your context parameter.
         * To call it, you can `summon` it.
         */
        def fold[A](init: A)(l: List[A])(using Combinator[A]): A = l.fold(init)(summon[Combinator[A]].combine)

        check(fold(0)(List(1, 2, 4))(using intCombinator) == ??)
        check(fold(0)(List(1, 2, 4)) == ??)
      }

      exercise("context bound", activated = true) {

        /** You still can use context bound instead of using. */
        def fold[A: Combinator](init: A)(l: List[A]): A = l.fold(init)(summon[Combinator[A]].combine)

        check(fold(0)(List(1, 2, 5)) == ??)
      }
    }

}

object _03_typeclass {

  trait Combinator[A] {
    extension (lhs: A) def combine(rhs: A): A
  }

  given Combinator[Int] with {
    extension (lhs: Int) override def combine(rhs: Int): Int = lhs + rhs
  }

  def fold[A: Combinator](init: A)(l: List[A]): A = l.fold(init)(_.combine(_))

  @main
  def _01_tc(): Unit =
    section("PART 3 - Typeclass") {
      println(1.combine(2))
    }

  @main
  def _02_tc(): Unit =
    section("PART - Create your own typeclass instance") {
      exercise("???", activated = false) {
        given Combinator[String] with {
          extension (lhs: String) override def combine(rhs: String): String = ???
        }

        check(fold("")(List("Hello", "World")) == "HelloWorld")
      }
    }

  @main
  def _03_tc(): Unit =
    section("PART - ") {
      exercise("???", activated = false) {

        /**
         * Show is a typeclass that adds the operation `show` to types
         * in a view to get the String representation of their values.
         */
        trait Show[A] {
          extension (a: A) def show: String
        }

        /**
         * Uncomment the lines below one by one and implement the
         * necessary given instances.
         */

//        check(1.show == "1")
//        check("hello".show == "hello")
//        check(List(1, 2, 3).show == "[1,2,3]")
//        check(Option(List(1, 2, 3)).show == "Some([1,2,3])")
      }
    }
}

object _04_implicit_conversion {

  case class Rational(n: Int, d: Int) { self =>
    def *(other: Rational): Rational = Rational(n * other.n, d * other.d)
  }

  /**
   * Scala 3 provides a Conversion typeclass to handle implicit
   * conversion. Here we define a way to combine seemlessly rational
   * numbers with int providing to the compiler a way to convert an int
   * into rational number.
   *
   * In Scala 2, we had to use implicit def instead.
   */
  given Conversion[Int, Rational] with
    def apply(int: Int): Rational = Rational(int, 1)

  @main
  def _04_implicit(): Unit =
    section("PART 4 - Implicit conversion") {
      check(Rational(3, 1) * 2 == Rational(6, 1))
    }
}
