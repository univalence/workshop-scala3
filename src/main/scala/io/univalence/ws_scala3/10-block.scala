package io.univalence.ws_scala3

import io.univalence.ws_scala3.internal.exercise_tools.*

/**
 * =Code-block delimitation=
 * Scala 3 comes with new syntaxes to delimit code-blocks. Here, we will
 * first focus on function body, before taking a look the other
 * constructs.
 *
 * In terms of new syntax, for exemple, braces for the function body are
 * not necessary anymore, if the function has many lines. So, it is up
 * to you to add or not those braces.
 *
 * In the case, you do not add braces, the compiler becomes
 * indentation-sensitive — meaning that the indentation defines the
 * code-block. Python has the same functionality.
 *
 * You can also put an end marker to a function (like with Ruby) or use
 * braces back again. If you stay consistent in terms of code-block
 * delimitation, you can mix the different syntaxes.
 *
 * So, this is an example with braces.
 */
@main
def _10_01_a_with_braces(): Unit = {
  val hoursInDay  = 24.0
  val daysInWeek  = 7.0
  val hoursInWeek = hoursInDay * daysInWeek

  println(s"There is $hoursInWeek hours in a week")
}

/** This is an example without braces. */
@main
def _10_01_b_without_braces(): Unit =
  val hoursInDay  = 24.0
  val daysInWeek  = 7.0
  val hoursInWeek = hoursInDay * daysInWeek

  println(s"There is $hoursInWeek hours in a week")

/**
 * You can also use a tag to indicate the end of your code-block, like
 * Ruby. In this approach, the compile is not indentation-sensitive.
 *
 * Pay attention to the name you put after the end marker. It should fit
 * exactly the name of the function. Typically, you may have
 * hard-to-solve-stupid-compilation-errors if you change the name of the
 * function by hand.
 */
@main
def _10_01_c_with_markup(): Unit =
  val hoursInDay  = 24.0
  val daysInWeek  = 7.0
  val hoursInWeek = hoursInDay * daysInWeek

  println(s"There is $hoursInWeek hours in a week")
end _10_01_c_with_markup

/**
 * ==Confusion with indentation==
 *
 * There is a huge drawback with the new syntax. The fact that
 * indentation is significant may lead to confusing errors, not only
 * compile errors, but also business errors.
 *
 * Here is an example...
 */

def plusOne(value: Int) =
  value + 1

  def plusTwo(value: Int) = value + 2

@main
def _10_02_indentation_confusion(): Unit =
  section("Confusion with indentation") {
    exercise("Try to guess the value", activated = true) {
      // The expected value below might not be the one you was thinking of...
      // But why? Try to fix the code above.
      check(plusOne(10) == ??)
    }
  }

/**
 * ==Syntax conversion==
 * There is also new syntaxes available for other kind of constructs in
 * Scala 3, like if expression, for-comprehension, object declaration...
 *
 * Now that you have seen the different syntaxes for functions, you are
 * ready to understand those new syntaxes for other kind of Scala 3
 * constructs.
 *
 * TODO Below, convert [[MyObject]] and its content so it match the
 * Scala 2 syntax.
 */

// Guess what, this is the Python-like syntax for object. You have the
// same for trait, class, case class...
object MyObject:
  val a = 42

  def signOf(value: Int): Int =
    if value < 0 then -1
    else if value > 0 then 1
    else 0
    end if
  end signOf

  def addOptions(oa: Option[Int], ob: Option[Int]): Option[Int] =
    for
      a <- oa
      b <- ob
    yield a + b

  def printListLn(l: List[Int]): Unit =
    for a <- l
    do println(a)

  def roleOf(name: String): String = {
    name match
      case "Jon"                     => "Product owner"
      case "Mary"                    => "Scrum master"
      case "Tom" | "Mark" | "Jessie" => "Developer"
      case _                         => "unknown"
    end match
  }

@main
def _10_03_syntax_conversion(): Unit =
  section("Syntax conversion") {
    exercise("By changing the syntax of MyObject by hand, check that the result stay the same", activated = true) {
      check(MyObject.a == 42)

      check(MyObject.signOf(42) == 1)
      check(MyObject.signOf(-42) == -1)
      check(MyObject.signOf(0) == 0)

      check(MyObject.addOptions(Some(1), None).isEmpty)
      check(MyObject.addOptions(Some(1), Some(2)).contains(3))

      check(MyObject.roleOf("Jon") == "Product owner")
      check(MyObject.roleOf("Jessie") == "Developer")
      check(MyObject.roleOf("David") == "unknown")
    }
  }

/**
 * What we have seen with code-block delimitation is also applicable for
 * many constructions: class, case class, trait, variable declaration...
 *
 * It is up to you (and your team) to choose the style you want to
 * adopt.
 *
 * Note that Scala 3 proposes different compilation options to force the
 * adoption of a block style or another (`-new-syntax`, `-old-syntax`,
 * `-indent`, `-no=indent`). If necessary, the compiler can rewrite your
 * blocks so they match the selected style.
 */
