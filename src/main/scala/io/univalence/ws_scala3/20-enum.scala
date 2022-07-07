package io.univalence.ws_scala3

import io.univalence.ws_scala3.internal.exercise_tools._

/**
 * =Enum=
 * Scala 3 introduces a new construct to declare a enumeration, and even
 * an ADT or a GADT. In comes with capabilities almost unique in the
 * context of the JVM.
 *
 * In this file, you will see different usages of the `enum` construct
 * in Scala 3, from the one that mimics Java approach to the complete
 * building of an ADT.
 */
object _01_simple_enum {

  /**
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
   * Info: in reality, Scala enums is a syntaxic sugar that generate
   * under the hood for our old Scala 2 way of describing enumeration
   * with several helper functions.
   */

  @main
  def _01_Colors(): Unit =
    section("PART 1 - color enumeration") {
      println(s"Red: ${Color.RED}")

      /**
       * Scala enums almost respect the Java contract (`values` returns
       * an array).
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
      check(Color.values.mkString(", ") == ??)
      check(Color.valueOf("BLUE") == ??)
      check(Color.fromOrdinal(1) == ??)
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

object _02_arithmetic_expression {

  /**
   * Scala enums such as Colors is composed by singleton cases.
   *
   * But we can also have parametrize cases. It allows us to create
   * ADTs.
   *
   * For the example we will create an ADT that describe a function such
   * as: f(x) = x + x + become Add(Add(Var,Var),Const(10))
   */
  enum Arithmetic:
    case Var
    case Const(a: Int)
    case Add(left: Arithmetic, right: Arithmetic)

    def +(other: Arithmetic): Add = Add(this, other)

    def apply(x: Int): Int =
      this match
        case Var       => x
        case Const(v)  => v
        case Add(l, r) => l(x) + r(x)

  @main
  def _02_Arithmetic(): Unit =
    section("PART 2 - arithmetic expression") {
      import Arithmetic.*

      val f = Var + Var + Const(10)
      check(f(20) == ??)
    }
}

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
  }

  @main
  def _03_Notes(): Unit =
    section("PART 3 - enum with constructor") {
      check(Note.A4.frequency == ??)
    }
}

object _04_adt {

  /**
   * Enumeration in Scala also support type parameters implying the
   * creation of GADTs.
   *
   * As an example, we can reproduce the Option ADT using Scala 3
   * enumerations.
   */
  enum PeutEtre[+A] {
    case Present(a: A)
    case Rien

    def isDefined: Boolean =
      this match
        case Rien => false
        case _    => true

    def orElse[B >: A](b: B): B =
      this match
        case Rien       => b
        case Present(v) => v
  }

  @main
  def _04_ADT_with_enum(): Unit =
    section("PART 4 - GADT") {
      import PeutEtre.*

      check(Present("123").isDefined == ??)
      check(Rien.isDefined == ??)
      check(Rien.orElse(10) == ??)
    }

  /**
   * However enumerations don't replace entirely the ADTs created using
   * sealed traits. Indeed, for example, you can't specify custom
   * functions for each enumeration cases.
   */

  // This code doesn't not compile.
  //enum Cases:
  //  case FIRST {
  //    def doSomething(): Unit = ???
  //  }
  //  case SECOND {
  //    def doSomethingElse(): Unit = ???
  //  }

  sealed trait Cases
  object Cases {
    case class First {
      def doSomething(): Unit = println("Something")
    }
    case class Second {
      def doSomethingElse(): Unit = println("Something else")
    }
  }

}
