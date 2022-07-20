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