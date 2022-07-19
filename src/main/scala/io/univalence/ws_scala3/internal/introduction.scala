package io.univalence.ws_scala3.internal

import io.univalence.ws_scala3.internal.exercise_tools._

/**
 * =Introduction=
 * This workshop is built with the help of Scala 3's macro. Those macros
 * are located in [[exercise_tools]].
 *
 * The workshop is divided into Scala files with a number (with a
 * BASIC-like numeration).
 *
 * In those files, except the first one, you will find one or more
 * top-level object, having one or more ''main'' functions.
 */
object introduction {

  /**
   * In a main function, you will find one or more sections. Each
   * section comes with a title that is displayed in the program output
   * (in addition to the file name and the line number of the section).
   */
  @main
  def _00_introduction(): Unit = {
    section("PART 1 - This a first part") {

      /**
       * Each section is composed of exercises or sub-sections. For
       * exercises, there is also a title. This one is also displayed in
       * the program output. You have also another parameter named
       * `activated`, that we will later.
       */
      exercise("A first exercise", activated = true) {
        val a = 42

        /**
         * An exercise used to come with one or more check-lines. Each
         * check-line comes with a boolean expression.
         *
         * If the expression is `true`, the display shows the expression
         * and a OK message.
         *
         * If the expression is `false`, the display shows the
         * expression and a short failure message (you have to guess why
         * ;) )
         */
        check(a == 42)
      }

      exercise("A second exercise", activated = true) {
        val b = 24

        /**
         * Some check-lines come with the placeholder `??`. It always
         * fails the boolean expression. And so, you have to guess the
         * value!
         */
        check(b == ??)
      }

    }

    section("PART 2 - This a second part") {

      /**
       * Some exercise are semantically correct, but they produce and
       * exception at runtime. In this case, the exercise is deactivated
       * (meaning, the `activated` parameter is set to `false`). A
       * specific message appears in the output if you try to run the
       * program.
       *
       * So, every time that you see a deactivated exercise, you have to
       * set the `activated` parameter to `true` and solve the exercise.
       */
      exercise("Deactivated exercise", activated = false) {
        val c: Int = ???

        check(c * 2 == 6)
      }

      /**
       * Some exercises are semantically incorrect and results in
       * failure at compile-time. To avoid this, the only solution is to
       * comment out the content of those exercises, and TODO tag is
       * added.
       *
       * So, every time that you see body of an exercise commented out,
       * you have to uncomment the body and solve the exercise.
       */
      exercise("Exercise with commented content", activated = false) {
        // TODO uncomment the lines below and solve the exercise.
//        object d {}
//
//        check(d.a == 42)
      }
    }
  }

}
