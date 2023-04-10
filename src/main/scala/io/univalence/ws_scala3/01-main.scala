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
 * =Hello Scala 3=
 * This is your first program in Scala 3! Well, it's even your first
 * program'''''s''''' in Scala 3. As you will see, we can declare many
 * main toplevel functions in a single file.
 *
 * So, in this file, you will see alternative ways to declare a main
 * function introduced in Scala 3.
 *
 * ==Main==
 * Now, main can be a toplevel function.
 *
 * To declare a main function, you just have to declare a function with
 * no toplevel structure (object or class) and add the `@main`
 * annotation. The main function can have any name.
 *
 * To launch this main (see readme for detailled instructions):
 *   - under IntelliJ IDEA, just click on the green arrow in the gutter
 *     just below, then click "Run".
 *   - under VS Code (with Metals extension), click on the `run` button 
 *     above the @main annotation
 *   - under SBT, notice this filename and the main function below. Type
 *     `run` and select the item you are looking for in the menu.
 */
@main
def _01_01_main(): Unit = println("Hello world")

/** You can add as many main function as you want in a single file. */
@main
def _01_02_main(): Unit = println("dlrow olleH")

/**
 * ===Notes===
 * Main toplevel function may have parameters. You can use the usual
 * `args: Array[String]`, or use the new vararg syntax `args: String*`.
 * It is also possible to have more specific argument types, like `ip:
 * String, maxRetry: Int`. In this case, your program will fail if the
 * second argument is not an integer.
 */

/**
 * Below is a main function inside an object. This is the old way in
 * Scala (ie. Scala 2) to declare a main.
 */
object _01_03_main {
  def main(args: Array[String]): Unit = println("Hello world! (old way)")
}
