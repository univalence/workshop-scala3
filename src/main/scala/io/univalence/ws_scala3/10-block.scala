package io.univalence.ws_scala3

import io.univalence.ws_scala3.internal.exercise_tools.*

/**
 * =Code-block delimitation=
 * Braces for the function body are not necessary anymore if it has many
 * lines. So, it is up to you to add or not those braces.
 *
 * In the case, you do not add braces, the compiler becomes
 * indentation-sensitive — meaning that the indentation defines the
 * code-block. Python has the same functionality.
 *
 * So, this is an example with braces.
 */
@main
def _01_a_with_parentheses(): Unit = {
  val hoursInDay  = 24.0
  val daysInWeek  = 7.0
  val hoursInWeek = hoursInDay * daysInWeek

  println(s"There is $hoursInWeek hours in a week")
}

/** This is an example without braces. */
@main
def _01_b_with_parentheses(): Unit =
  val hoursInDay  = 24.0
  val daysInWeek  = 7.0
  val hoursInWeek = hoursInDay * daysInWeek

  println(s"There is $hoursInWeek hours in a week")

/**
 * You can also use a tag to indicate the end of your code-block, like
 * Ruby. In this approach, the compile is not indentation-sensitive.
 */
@main
def _01_c_with_parentheses(): Unit =
  val hoursInDay  = 24.0
  val daysInWeek  = 7.0
  val hoursInWeek = hoursInDay * daysInWeek

  println(s"There is $hoursInWeek hours in a week")
end _01_c_with_parentheses

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
def _02_indentation_confusion(): Unit =
  section("Confusion with indentation") {
    // The expected value below might not be the one you was thinking of...
    check(plusOne(10) == ??)
  }

/**
 * ==Syntax conversion==
 * There is also a new syntax available for other kind of constructs in
 * Scala 3, like if expression, for-comprehension, object declaration...
 *
 * TODO Below, convert [[MyObject]] and its content so it match the
 * Scala 2 syntax.
 */

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

@main
def _03_syntax_conversion(): Unit =
  section("Syntax conversion") {
    check(MyObject.a == 42)

    check(MyObject.signOf(42) == 1)
    check(MyObject.signOf(-42) == -1)
    check(MyObject.signOf(0) == 0)

    check(MyObject.addOptions(Some(1), None).isEmpty)
    check(MyObject.addOptions(Some(1), Some(2)).contains(3))
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
