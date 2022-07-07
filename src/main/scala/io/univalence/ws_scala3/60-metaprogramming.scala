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
 * at compile-time. This gives you access to code optimization, in
 * addition to the one provided by the JIT.
 *
 * You can inline functions, variables, parameters, match-structures,
 * and if-structures.
 *
 * The problem of inlining in Scala is not really visible once applied
 * by the compiler, unless you do a before-after comparaison of the
 * produced bytecode, or unless it produces a compilation error. But we
 * can see its action, especially with inline if and inline recursion.
 */

object _01_inlining {

  /**
   * ==Inline if==
   * It is possible to inline an if. In this case, Scala will analyse
   * the if at compile-time. If the condition can be evaluated at
   * compile-time, Scala will replace the whole if by the computed
   * value. If not, the compiler fails.
   */

  enum Sign {
    case Negative, Zero, Positive
  }

  inline def signOf(value: Int): Sign =
    inline if (value == 0) Sign.Zero
    else inline if (value > 0) Sign.Positive
    else Sign.Negative

  @main
  def _01_inline_condition(): Unit =
    section("PART 1 - inline if") {
      exercise("working example with inline if", activated = true) {
        check(signOf(0) == ??)
        check(signOf(-1) == ??)
        check(signOf(1) == ??)
      }
      exercise("example that crash at compile-time", activated = false) {
        // TODO uncomment and try to compile the file
//        check(signOf("0".toInt) == Sign.Zero)
      }
    }

  /**
   * ==Recursive inline function==
   * If the inline function contains an inline if and is recursive, the
   * compiler will try to evaluate the function call and replace the
   * call by the computed value.
   *
   * But what happens if there are too many recursive calls? In this, it
   * does not end up with a StackOverflowError. Instead, you will a
   * compiler failure.
   */

  inline def sumUpTo(value: Int): Int =
    inline if (value == 0) 0
    else value + sumUpTo(value - 1)

  @main
  def _02_inline_recursion(): Unit =
    section("PART 2 - inline recursion") {
      exercise("working example with inline recursion", activated = true) {
        check(sumUpTo(0) == ??)
        check(sumUpTo(1) == ??)
        check(sumUpTo(2) == ??)
        check(sumUpTo(10) == ??)
      }

      exercise("at which value does the compiler fails?", activated = false) {
        // TODO uncomment those lines one by one. run the program for every uncommented line.
        //    check(sumUpTo(30) == 465)
        //    check(sumUpTo(31) == 496)
        //    check(sumUpTo(32) == 528)
        //    check(sumUpTo(33) == 561)
      }
    }

  /**
   * The depth of the compiler stack for inline recursive calls can be
   * changed with the compiler options `-Xmax-inlines`.
   */
}

/**
 * ==Transparent inline==
 * Inline resolution happens after the compiler has resolve types. There
 * may use cases where it is interested to act before the type
 * resolution and to force this resolution. This is what you get with
 * ''transparent inline''.
 */
object _02_transparent_inline {

  /**
   * A transparent inline function has an output type that should be
   * wider than its different returned value types.
   *
   * The output type can be expressed in terms of union type (`String |
   * Int | Boolean`). The effective type is determined at compile-time.
   */
  transparent inline def defaultOf(typeName: String): Any =
    inline typeName match {
      case "String"  => "hello"
      case "Int"     => 42
      case "Boolean" => true
    }

  @main
  def _01_transparent_inline(): Unit =
    section("PART 3 - Transparent inline") {
      exercise("Polyvalent function", activated = true) {
        check(defaultOf("String") + 5 == ??)
        check(defaultOf("Int") + 5 == ??)
        check(defaultOf("Boolean") || false == ??)
      }
    }
}
