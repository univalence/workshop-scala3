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
 * =Enum=
 * Scala 3 introduces a new construct to declare a enumeration, and even
 * an ADT, parameterized ADT, or a GADT. It comes with capabilities
 * almost unique in the context of the JVM.
 *
 * In this file, you will see different usages of the `enum` construct
 * in Scala 3, from the one that mimics Java approach to the complete
 * building of an ADT.
 */
object _01_simple_enum {

  /**
   * ==Simple enum==
   * A simple enum is just an enumeration of constants with a proper
   * identity. It comes with predefined members:
   *   - `[enum].values`: array of all available values in the enum
   *   - `[enum].valueOf(String)`: get a value from its name
   *   - `[enum].fromOrdinal(Int)`: get a value from its index in the
   *     enum
   *   - `[value].ordinal`: get the index of a value
   *
   * The enum below just represents some colors.
   */
  /**
   * In Scala 2 generally, we tend to write an enumeration as follows.
   */
  sealed trait OldColor
  
  object OldColor {
    case object RED   extends OldColor
    case object GREEN extends OldColor
    case object BLUE  extends OldColor
  }

  /** In Scala 3, we have Scala enums to replace the syntax. */
  enum NewColor:
    case RED extends NewColor
    case GREEN extends NewColor
    case BLUE extends NewColor

  /** The notation can be shortened. */
  enum Color:
    case RED, GREEN, BLUE

  /**
   * '''INFO''': in reality, Scala enums is a syntaxic sugar that
   * generates our old Scala 2 way of describing enumeration with
   * several helper functions under the hood for .
   */

  @main
  def _20_01_Colors(): Unit =
    section("PART 1 - color enumeration") {
      println(s"Red: ${Color.RED}")

      /**
       * Scala enums almost respects the Java contract (eg. `values`
       * returns an array).
       */
      println(s"Colors: ${Color.values.mkString(", ")}")
      println(s"Color value of BLUE: ${Color.valueOf("BLUE")}")
      println(s"Color from ordinal 1: ${Color.fromOrdinal(1)}")

      /**
       * The `check` method comes from a macro created for this
       * workshop. Your are asked to replaced the placeholder `??` with
       * the requested value.
       *
       * Note: if you run this program and take a look at the output,
       * you will easily answer those checks ;)
       */
      comment("What is the list of available colors?")
      check(Color.values.mkString(", ") == ??)
      comment("What is the string value of the color BLUE?")
      check(Color.valueOf("BLUE") == ??)
      comment("What is the color at position 1 in the enum type?")
      check(Color.fromOrdinal(1) == ??)
      comment("What is the position of the color RED in its enum type?")
      check(Color.RED.ordinal == ??)
    }

  /**
   * To ensure compatibility of a Scala enum in a Java context, you only
   * have to extends your enum with [[java.lang.Enum]].
   */
  enum JavaColor extends java.lang.Enum[JavaColor] {
    case RED, GREEN, BLUE
  }
}

/**
 * ==ADT==
 * Scala enums such as Colors is composed by singleton cases.
 *
 * But we can also have parametrized cases. It allows us to create ADTs.
 *
 * For the example, we will create an ADT that describes a function,
 * such as `f(x) = x + x + 10` as the expression
 * `Add(Add(Var,Var),Const(10))`.
 */
object _02_arithmetic_expression {

  enum Arithmetic:
    case Var
    case Const(a: Int)
    case Add(left: Arithmetic, right: Arithmetic)

    def +(other: Arithmetic): Add = Add(this, other)

    def show: String =
      this match
        case Var       => "X"
        case Const(v)  => v.toString
        case Add(l, r) => l.show + " + " + r.show

    def apply(x: Int): Int =
      this match
        case Var       => x
        case Const(v)  => v
        case Add(l, r) => l(x) + r(x)

  @main
  def _20_02_Arithmetic(): Unit =
    section("PART 2 - arithmetic expression") {
      import Arithmetic.*

      val f = Var + Var + Const(10)

      comment(s"What is the value of the expression '${f.show}' for ${Var.show} = 20?")
      check(f(20) == ??)
    }
}

/**
 * ==Parameterized enum==
 * We might want to associate each item of enum to specific values. This
 * can be done in the same way as Java.
 */
object _03_enum_with_constructor {

  /** We can parametrize enumeration. */
  enum Note(val frequency: Double) {
    case C4 extends Note(261.63)
    case D4 extends Note(293.66)
    case E4 extends Note(329.63)
    case F4 extends Note(349.23)
    case G4 extends Note(392.0)
    case A4 extends Note(440.0)
    case B4 extends Note(493.88)

    /**
     * The Note parameters allows us to create generic functions that
     * works for any Notes.
     */
    def time: Double = 1.0 / frequency.toFloat

    def octave(n: Int): Double = frequency * Math.pow(2.0, n - 4)
  }

  @main
  def _20_03_Notes(): Unit =
    section("PART 3 - enum with constructor") {
      comment("What is the frequency of the note A4?")
      check(Note.A4.frequency == ??)
      comment("What is the time period of the note A4?")
      check(Note.A4.time == ??)
      comment("What is the frequency of the note (A5) of 1 octave above A4?")
      check(Note.A4.octave(5) == ??)
      comment("What is the frequency of the note (A3) of 1 octaves below A4?")
      check(Note.A4.octave(3) == ??)
    }
}

/**
 * ==ADT==
 * Enumeration in Scala also support type parameters implying the
 * creation of ADTs. In this section, we will see what a ADT refers to.
 *
 * As an example, we can reproduce the Option ADT using Scala 3
 * enumerations.
 */
object _04_adt {

  /**
   * This enum type is similar to `Option`. The supertype `PeutEtre` is like a
   * container of a value of type `A` (the type of the value inside the
   * `Present` subtype). `Rien` represents an absence of value while `Present`
   * represents the contrary.
   */
  enum PeutEtre[A] {
    // This is the actual container of a value  of type `A`
    case Present(a: A)

    // This is a empty container
    case Rien()

    def isDefined: Boolean =
      this match
        case Rien() => false
        // default case (`Present`)
        case _    => true
      end match

    /**
     * `:>` express a inheritance relationship, here `B` of `map` needs to be
     * the subtype of `A` (type of `PeutEtre`).
     */
    def orElse[B >: A](b: B): B =
      this match
        case Rien()     => b
        case Present(v) => v
  }

  @main
  def _20_04_ADT_with_enum(): Unit =
    section("PART 4 - GADT") {
      import PeutEtre.*

      check(Present("123").isDefined == ??)
      check(Rien().isDefined == ??)
      check(Rien().orElse(10) == ??)
    }

  /**
   * However enumerations don't replace entirely the ADTs created using
   * sealed traits. Indeed, for example, you cannot specify custom
   * functions for each enumeration cases.
   */
  // This code doesn't not compile.
  // enum Cases:
  //  case First {
  //    def doSomething(): Unit = println("Something")
  //  }
  //  case Second {
  //    def doSomethingElse(): Unit = println("Something else")
  //  }

  sealed trait Cases

  object Cases:
    case object First extends Cases:
      def doSomething(): Unit = println("Something")
    case object Second extends Cases:
      def doSomethingElse(): Unit = println("Something else")
  end Cases
}

/**
 * ==GADT==
 * GADTs stands for Generalised ADTs. This is a special version of ADTs.
 * We will see that with an example.
 *
 * As an example, we want to modelize a Map-Reduce engine to execute distributed
 * computations using Scala 3 enumerations. Our engine will support the
 * operations `Map` and `Reduce`:
 * - `Map`: will execute a given function on a given computation. The computed value
 *    by the engine will be passed to the function.
 * - `Reduce`: will take two computation, get the computed value of each of them,
 *   and will then run the given function to combine the 2 values into one.
 * 
 * We will have an additional operation that will load a data on disk and keep
 * them in memory for future computations. Read data will be represented as List.
 */
object _05_gadt {

  /**
   * This is our Engine supertype that modelize a distributed Map/Reduce
   * computation. Note that we have a generic `A` for the value returned
   * by the computation.
   * For simplicity, we will consider for our example that our Map/Reduce
   * engine will only do computations list of strings (i.e. computations
   * will only be on string).
   */
  enum Computation[A] {
    /**
     * This represent data load on disk into memory. We can read multiple blocks
     * of data for a distributed computation. It will be wise to splice the data
     * into mutltiple blocks to avoid to exceed computer memory.
     * 
     * Note that in the case of `Block`, as a list of strings has be loaded the
     * return type will be `List[String]`. With a GADT, when defining a subtype,
     * it can extends with a more specific type.
     */
    case Block(value: List[String]) extends Computation[List[String]]

    /**
     * This a `Map` operation on a computation (`arg`) on which we want the
     * function `fun` to be applied to the result of that computation. Note
     * that the function could change the return type of the computation from
     * a type `C` (type of the initial computation) to a type `D` (type of the
     * mapping result).
     * 
     * The type `C` will be unknown outside of `Map` (i.e. if we have
     * a value of type `Map` we can't retrieve the type of `C`). This is 
     * also specific to GADTs.
     */
    case Map[C, D](arg: Computation[C], fun: C => D) extends Computation[D]

    /**
     * This is a `Reduce` operation on two computations, i.e. we want to
     * combine the results of the 2 computations into a single value. To 
     * combine the computed values we will use the given function `fun`.
     */
    case Reduce[X](arg1: Computation[X], arg2: Computation[X], fun: (X, X) => X)
        extends Computation[X]
  }

  object Computation {
    /**
     * This method evaluate a computation by handling the different cases
     * (Map / Reduce / Block). This function will return a result of the type
     * `A` (the generic of `Computation`). The result type will change according
     * to the case we are handling in our pattern-matching.
     */
    def eval[A](computation: Computation[A]): A = computation match {
      /**
       * When having a Block, we just have to return its value. That result
       * will be of type `List[A]`. The compiler will be able to deduce that
       * in this case `A == List`.
       */
      case Block(value)            => value: List[String] // we assert that the result is of type `List[String]`

      /**
       * When having a `Map`, we extract the attributes (the computation and
       * the mapping function). Then we eval the computation to get the result
       * and apply it to the mapping function.
       * Note that:
       * - we don't know the type of `arg`(it's hidden), all we can do with it
       *   is to pass it to the mapping function.
       * - the return type is the return type of the mapping function (`A`)
       */
      case Map(arg, fun)           => fun(eval(arg)): A // we assert that the result is of type `A`

      /**
       * When having a reduce, we extract the attributes (the 2 computations
       * and the combining function). Then we eval the computations and we
       * pass the results to the combining function. The result will be of type `A`.
       */
      case Reduce(arg1, arg2, fun) => fun(eval(arg1), eval(arg2)): A // we assert that the result is of type `A`
    }
  }

  @main
  def _20_05_GADT_with_enum(): Unit =
    section("PART 5 - GADT") {
      // We import all the subtypes + eval
      import Computation.*

      exercise("GADT 1: inversed list", activated = true) {
        /**
         * This is our 1st Map/Reduce program.
         * 
         * We have a list of strings that we want to split in 2 blocks,
         * reverse each block and concat the blocks to get original list in
         * the reverse order. We use `reverse` in the mapping function to
         * reverse a list and `concat` method (on List) to merge 2 lists.
         */
        val reversed: Computation[List[String]] = Reduce(
          Map(Block(List("Hello", "my", "friend")), x => x.reverse),
          Map(Block(List("Nice", "seeing", "you")), x => x.reverse),
          (a, b) => b.concat(a)
        )
  
        // What we will be the return type here ?
        check(eval(reversed) == ??)
      }

      exercise("GADT 2: wordcount", activated = true) {
        // We have a list of words that we want to split in 2 blocks.
        val words = List("Welcome", "to", "the", "party", "The", "world", "is", "yours")

        /**
         * We want to implement a wordcount.
         * 
         * From a list of words, we split the list in 2. Then we want to count
         * the words of each list and sum all the counts. `size` method give
         * the word count of a block and we just use `+` as combining function.
         */
        def wordCount(input: List[String]): Computation[Int] = {
          val window = 4
          val (block1, block2) = (Block(input.take(window)), Block(input.drop(window)))
          Reduce(
            Map(block1, word => word.size),
            Map(block2, word => word.size),
            (a, b) => a + b
          )
        }

        // We get the distributed program we need to run
        val wcProgram = wordCount(words)
  
        // And we eval our program
        check(eval(wcProgram) == ??)
      }

    }
}
