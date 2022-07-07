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
  enum Color {
    case RED, GREEN, BLUE
  }

  @main
  def _01_Colors(): Unit =
    section("PART 1 - Color enumeration") {
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

object _02_enum_with_constructor {

  enum Note(val frequency: Double) {
    case C4 extends Note(261.63)
    case D4 extends Note(293.66)
    case E4 extends Note(329.63)
    case F4 extends Note(349.23)
    case G4 extends Note(392.0)
    case A4 extends Note(440.0)
    case B4 extends Note(493.88)
  }

  @main
  def _02_Notes(): Unit =
    section("PART 2 - enum with constructor") {
      check(Note.A4.frequency == ??)
    }

}

object _03_adt {

  enum PeutEtre[+A] {
    case Present(a: A)
    case Rien

    def isDefined: Boolean =
      this match
        case Rien => false
        case _    => true
  }

  @main
  def _03_ADT_with_enum(): Unit =
    section("PART 3 - ADT") {
      check(PeutEtre.Present("123").isDefined == ??)
      check(PeutEtre.Rien.isDefined == ??)
    }

}
