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

import io.univalence.ws_scala3.internal.exercise_tools.*

/**
 * =Metaprogramming=
 *
 * Metaprogramming is the ability to execute a code that manipulates
 * another code. In Scala, this can be done at compile-time or at
 * runtime (reflection).
 *
 * For compile-time operation, you can use:
 *   - inlining
 *   - macro
 *   - other compile-time operations
 *
 * ==Inlining==
 * Here, we will see inlining. It is represented by the keyword `inline`
 * and it gives you the power of macros without using macros!
 *
 * The principle of inlining is to replace a call-site by its computed
 * value at compile-time. This gives you access to additional code
 * optimization, in addition to the one provided by the JIT.
 *
 * You can inline functions, variables, parameters, match-structures,
 * and if-structures.
 *
 * The problem in making exercises with inlining in Scala is that this
 * feature does not really give visible results once applied by the
 * compiler, unless you do a before-after comparaison of the produced
 * bytecode, or unless it produces a compilation error. We will focus on
 * the last case.
 */

object _60_inlining {

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

  /**
   * To use an inline-if, you have first to define an
   * ''inline-function''.
   */
  inline def signOf(value: Int): Sign =
    inline if (value == 0) Sign.Zero
    else inline if (value > 0) Sign.Positive
    else Sign.Negative

  @main
  def _60_01_inline_condition(): Unit =
    section("PART 1 - inline if") {
      exercise("working example with inline if", activated = true) {
        check(signOf(0) == ??)
        check(signOf(-1) == ??)
        check(signOf(1) == ??)
        check(signOf((5 + 6) * (7 - 12)) == ??)
      }

      exercise("example that fails at compile-time", activated = false) {
        // TODO activate the exercise, uncomment the line below and try to compile the file
//        check(signOf("0".toInt) == Sign.Zero)

        // >>> You can compile the file simply by trying to run it or
        // >>> by using `sbt compile` or `sbt run`.
      }

      /**
       * Scala 3 also defines the notion of ''inline-variable''. This
       * kind of variable can be use in expression using inline-if.
       */
      exercise("example that works with inline variable", activated = true) {
        inline val a = 42

        comment("What is the sign of the inline variable a?")
        check(signOf(a) == ??)
      }

      exercise("example that fails at compile-time with normal variable", activated = false) {
        val a = 42
        comment("What is the sign of the non-inline variable a?")
        // TODO activate the exercise, uncomment this line and see what happens
//        check(signOf(a) == ??)
      }
    }

  /**
   * The inline-if forces the developer to use "simple" expressions. By
   * "simple", we mean an expression that can be compute by the compiler
   * only.
   */

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
  def _60_02_inline_recursion(): Unit =
    section("PART 2 - inline recursion") {
      exercise("working example with inline recursion", activated = true) {
        check(sumUpTo(0) == ??)
        check(sumUpTo(1) == ??)
        check(sumUpTo(2) == ??)
        check(sumUpTo(10) == ??)
      }

      exercise("at which value does the compiler fails?", activated = false) {
        // TODO uncomment those lines one by one. Run the program for every uncommented line.
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
 * Inline resolution happens after the compiler has resolved types.
 * There may be use cases where it is interested to act before the type
 * resolution happens and to force this type resolution. This is what
 * you get with ''transparent inline''.
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
  def _60_03_transparent_inline(): Unit =
    section("PART 3 - Transparent inline") {
      exercise("Polyvalent function", activated = true) {
        // This works because the compiler converts the output type of defaultOf into an Int
        check(defaultOf("Int") + 5 == ??)

        // This works because the compiler converts the output type of defaultOf into an String
        check(defaultOf("String") + 5 == ??)

        // This works because the compiler converts the output type of defaultOf into an Boolean
        check((defaultOf("Boolean") || false) == ??)
      }
    }
}
