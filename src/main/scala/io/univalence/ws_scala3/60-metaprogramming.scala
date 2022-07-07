package io.univalence.ws_scala3

import io.univalence.ws_scala3.internal.exercise_tools._

/**
 * =Metaprogramming=
 *
 * Metaprogramming is the ability to execute your code to manipulate
 * your code. In Scala, this can be done at compile-time or at runtime
 * (reflection).
 *
 * For compile-time operation, you can use:
 *   - inlining
 *   - compile-time operation
 *   - macro
 *
 * ==Inlining==
 * Here we will see inlining. It is represented by the keyword `inline`
 * and it gives you the power of macros without using macros! The
 * principle of inlining is to replace a call-site by its computed value
 * at compile-time.
 *
 * The problem of inlining in Scala is not really visible, unless you do
 * a before-after comparaison of the produced bytecode, or unless it
 * produces a compilation error.
 */

object _01_inlining {

  enum Sign {
    case Negative, Zero, Positive
  }

  inline def signOf(value: Int): Sign =
    inline if (value == 0) Sign.Zero
    else inline if (value > 0) Sign.Positive
    else Sign.Negative

  @main
  def _01_inline_condition(): Unit = {
    check(signOf(0) == ??)
    check(signOf(-1) == ??)
    check(signOf(1) == ??)

//    check(signOf("0".toInt) == Sign.Zero)
  }

  def sumUpTo(value: Int): Int =
    if (value == 0) 0
    else value + sumUpTo(value - 1)

  @main
  def _02_inline_recursion(): Unit = {
    check(sumUpTo(0) == ??)
    check(sumUpTo(1) == ??)
    check(sumUpTo(2) == ??)
    check(sumUpTo(10) == ??)

//    check(sumUpTo(30) == 465)
//    check(sumUpTo(31) == 496)
//    check(sumUpTo(32) == 528)
//    check(sumUpTo(33) == 561)
  }
}
