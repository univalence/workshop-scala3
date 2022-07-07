package io.univalence.ws_scala3.internal

import io.univalence.ws_scala3.internal.exercise_tools.*

@main
def test2(): Unit = {
  check(1 + 1 == 2)
  check(1 + 1 == 3)

  check {
    val a = 1
    val b = 2
    a + b == 12
  }

  section("test 1") {
    exercise("exercise 1", activated = true) {
      check(1 + 1 == 11)
      check(1 + 1 == 2)
    }

    exercise("exercise 2", activated = true) {
      check(throw new IllegalArgumentException("oops"))
    }

    exercise("exercise 3", activated = false) {
      ???
    }
  }
}