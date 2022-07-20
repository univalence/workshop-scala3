/*
 * Copyright 2022 Univalence
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
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
 * The implicits still exist but we should use the new words because
 * they may be deprecated in the future.
 *
 * ==given and summon: contextual instance==
 */
object _01_given {

  /**
   * Combines two elements of the same type.
   *
   * This trait is like [[java.util.Comparator]] in Java. Combinator is
   * applicable a type `A`, on which you can call the method `combine`,
   * the same way you `Comparator` is applicable to a type A, on which
   * you can call the method `compare`.
   *
   * For example, `Combinator` can be applied to the type `Int`. In this
   * case, the `combine` would add two values of the type `Int`.
   *
   * From a mathematical point of view, `Combinator` is in a sense a
   * ''[[https://en.wikipedia.org/wiki/Semigroup semigroup]]''.
   *
   * @tparam A
   *   type on which Combinator can be applied.
   */
  trait Combinator[A] {
    def combine(lhs: A, rhs: A): A
  }

  /**
   * As it happens, we will create an instance of [[Combinator]] for the
   * type `Int`. As a usual developer, you will create an instance by
   * declaring a variable and using the keyword `new`, or by declaring
   * an ''object'':
   *
   * {{{
   *   val IntCombinator: Combinator[Int] = new Combinator[Int] { ... }
   *
   *   def combineAll[A](l: List[A])(combinator: Combinator[Int]): A = ???
   * }}}
   *
   * This instance is available by invoking explicitly the name
   * `IntCombinator`. But you might not want to use this name repeatedly
   * in your code, and thus to contextualize the instance (meaning to
   * make it implicitly available in the current scope). So, if you have
   * a good experience in Scala 2, you will use an implicit declaration.
   *
   * {{{
   *   implicit val IntCombinator: Combinator[Int] = new Combinator[Int] { ... }
   *
   *   def combineAll[A](l: List[A])(implicit combinator: Combinator[Int]): A = ???
   *
   *   // or, with context bound syntax
   *   def combineAll[A: Combinator](l: List[A]): A = ???
   *     // using here implicitly[Combinator[A]].combine(a, b)
   * }}}
   *
   * There are two problems here: 1/ the keyword `implicit` is also used
   * for other kind of use cases in Scala 2 (extension methods, implicit
   * conversion, context bound...), so it might lead to confusion if you
   * are a beginner to Scala, 2/ in fact, there is no need to put a name
   * to the implicit instances, as this name used to be avoided.
   *
   * Scala 3 comes with a specific keyword, named `given`, to declare
   * implicit instances for the use case of contextual abstraction.
   *
   * Here is the declaration of the instance of [[Combinator]] for the
   * type `Int`.
   */
  given Combinator[Int] with {
    override def combine(lhs: Int, rhs: Int): Int = lhs + rhs
  }

  /**
   * We also have another way to declare instances for [[Combinator]]
   * type by using a lambda expression.
   */
  given Combinator[String] = (lhs: String, rhs: String) => lhs + rhs

  /**
   * Note that you can give a name to an instance. It still can be
   * useful if you need several combinator for a same type.
   */
  given boolCombinator: Combinator[Boolean] with {
    override def combine(lhs: Boolean, rhs: Boolean): Boolean = lhs || rhs
  }

  /**
   * If you want to generify the declaration of an instance to a
   * parameterized type, you will see that the declaration is almost
   * unified, with a specificity in the declaration of the type
   * parameter.
   */
  given [A]: Combinator[List[A]] with {
    override def combine(lhs: List[A], rhs: List[A]): List[A] = lhs ++ rhs
  }

  @main
  def _40_01_given_and_summon(): Unit =
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
      }

      exercise("Combine 2 Booleans", activated = true) {

        /** We can style use a name if we want to. */
        check(boolCombinator.combine(true, false) == ??)
      }

      exercise("Combine 2 Lists of Ints", activated = true) {

        /** With a generic instance... */
        check(summon[Combinator[List[Int]]].combine(List(1, 2), List(3, 4)) == ??)
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

/**
 * ==using: contextual parameter==
 * Now, you want to capture in a function the instance declared in the
 * current scope.
 *
 * In Scala 2, you would use an implicit parameter (as seen in the above
 * section).
 *
 * Scala 3 introduces the keyword `using` for such usage.
 */
object _02_using {

  trait Combinator[A] {
    def combine(lhs: A, rhs: A): A
  }

  given intCombinator: Combinator[Int] with {
    override def combine(lhs: Int, rhs: Int): Int = lhs + rhs
  }

  @main
  def _40_02_using_in_function(): Unit =
    section("PART 2 - using") {
      exercise("using with named contextual parameter", activated = true) {

        /**
         * The `using` keyword helps to declare a contextual parameter
         * in a function, meaning a parameter, whose value may be
         * captured from the current or the outside scope.
         */
        def fold[A](init: A)(l: List[A])(using combinator: Combinator[A]): A = l.fold(init)(combinator.combine)

        /**
         * You can explicitly indicate the contextual parameter. In this
         * case, you must precede it with `using`.
         */
        check(fold(0)(List(1, 2, 3))(using intCombinator) == ??)

        /**
         * If you do not write the contextual parameter, Scala will look
         * up for an instance in the current and outside scope, that
         * satisfies the contextual parameter type at the call-site of
         * the function.
         */
        check(fold(0)(List(1, 2, 3)) == ??)

        /**
         * When you explicitly provide a contextual parameter, you have
         * to write the keyword `using`.
         *
         * The line below produces a compilation error.
         */
        // fold(0)(List(1, 2, 3))(intCombinator)
      }

      exercise("using with anonymous contextual parameter", activated = true) {

        /**
         * You don't have to specify a name for your contextual
         * parameter.
         *
         * So, in a view to use it, you have to `summon` it.
         */
        def fold[A](init: A)(l: List[A])(using Combinator[A]): A = l.fold(init)(summon[Combinator[A]].combine)

        check(fold(0)(List(1, 2, 4))(using intCombinator) == ??)
        check(fold(0)(List(1, 2, 4)) == ??)
      }

      exercise("context bound syntax", activated = true) {

        /**
         * You still can use the context bound syntax, like in Scala 2,
         * instead of declaring a contextual parameter with `using`.
         */
        def fold[A: Combinator](init: A)(l: List[A]): A = l.fold(init)(summon[Combinator[A]].combine)

        check(fold(0)(List(1, 2, 5)) == ??)
      }
    }

}

/**
 * ==Typeclass==
 * The Scala 3 syntax makes the declaration of typeclasses more
 * comfortable.
 *
 * But, you might not know what a typeclass is? A typeclass is a way to
 * categorize an existing type and to add new behavior on that type.
 *
 * The `Combinator` and `Comparator` are (in a sense) examples of
 * typeclasses. Indeed, in a global point of view of your code, with a
 * `Comparator`, you categorize a type as being comparable and with the
 * method `compare`, you add a new behavior to the categorized type, in
 * giving the possibility to compare its elements.
 *
 * The same goes for the `Combinator` type. But to make the typeclass
 * feature more usable while writing your code, in Scala 2, you would
 * use some tricky idioms, far from being accessible to every
 * developers.
 *
 * Below, we will see the way you declare a typeclass in Scala 3, with
 * the help of contextual instances, contextual parameters, and
 * extension method.
 */
object _03_typeclass {

  /**
   * Let's start again with the Combinator type. But this time, we want
   * that each combinable type gains the method `combine` (ie. you can
   * write `a.combine(b)` for every `a` and `b` of type `A`).
   */
  trait Combinator[A] {
    extension (lhs: A) def combine(rhs: A): A
  }

  /**
   * We create a contextual instance of Combinator for type Int with a
   * specific implementation.
   */
  given Combinator[Int] with {
    extension (lhs: Int) override def combine(rhs: Int): Int = lhs + rhs
  }

  /**
   * This is an example of function, that uses the typeclass Combinator.
   */
  def fold[A: Combinator](init: A)(l: List[A]): A = l.fold(init)(_.combine(_))

  @main
  def _40_03_combinator_typeclass_for_int(): Unit =
    section("PART 3 - Combinator typeclass for Int") {
      println(1.combine(2))
    }

  @main
  def _40_04_combinator_typeclass_for_string(): Unit =
    section("PART 4 - Create your own typeclass instance") {
      exercise("Give an implementation for the instance below", activated = false) {

        given Combinator[String] with {
          extension (lhs: String) override def combine(rhs: String): String = ???
        }

        check(fold("")(List("Hello", "World")) == "HelloWorld")
      }
    }

  given [A]: Combinator[List[A]] with {
    extension (lhs: List[A]) override def combine(rhs: List[A]): List[A] = ???
  }

  @main
  def _40_05_combinator_typeclass_for_list(): Unit =
    section("PART 5 - Create your own typeclass instance (bis)") {
      exercise("Give an implementation for the instance below", activated = false) {
        check(fold(List.empty[Int])(List(List(1, 2), List(3, 4), List(5, 6))) == List(1, 2, 3, 4, 5, 6))
      }
    }

  @main
  def _40_06_show_typeclass(): Unit =
    section("PART 6 - Create your own typeclass") {
      exercise("Try to solve this exercise by completing the typeclass Show below", activated = false) {

        /**
         * Show is a typeclass that adds the operation `show` to types
         * in a view to get the String representation of their values.
         */
        trait Show[A] {
          extension (a: A) def show: String
        }

        // TODO Uncomment the lines below one by one and implement the necessary given instances.

//        check(1.show == "1")
//        check("hello".show == "hello")
//        check(List(1, 2, 3).show == "[1,2,3]")
//        check(Option(List(1, 2, 3)).show == "Some([1,2,3])")
      }
    }
}

/**
 * ==Implicit conversion==
 * Scala 3 provides a Conversion typeclass to handle implicit
 * conversion. An implicit conversion allows you to implicitly define in
 * the current scope a function that is able to convert elements of a
 * type to elements to another type, if it is needed. As example, it is
 * almost natural to convert Int values to Double values in expressions
 * mixing both types of values.
 *
 * In Scala 2, we have to use `implicit def`. Scala 3 introduces the
 * typeclass Conversion. You thus only need to create instances from
 * this typeclass to declare what can be called contextual conversions.
 */
object _04_implicit_conversion {

  /** Represents rational number (of the form `n / d`). */
  case class Rational(n: Int, d: Int) { self =>
    def *(other: Rational): Rational = Rational(n * other.n, d * other.d)
  }

  /**
   * Here, we define a way to combine seamlessly rational numbers with
   * int, by providing to the compiler a way to convert an int into a
   * rational number.
   */
  given Conversion[Int, Rational] with
    def apply(int: Int): Rational = Rational(int, 1)

  @main
  def _40_07_implicit_conversion(): Unit =
    section("PART 7 - Implicit conversion") {
      check(Rational(3, 1) * 2 == Rational(6, 1))
    }
}
