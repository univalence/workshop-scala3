package io.univalence.ws_scala3

import io.univalence.ws_scala3.internal.exercise_tools.*

/**
 * =Hello Scala 3=
 * This is your first program in Scala 3! Well, it's even your first
 * programs in Scala 3. As you will see, we can declare many main
 * function in single file.
 *
 * In this file, you will see an alternative way to declare a main
 * introduced in Scala 3, and we will make a point about the block
 * delimitation syntaxes.
 *
 * ==Main==
 * Now, main can be a toplevel function.
 *
 * To declare a main function, you just have to declare a top-level
 * function and add the `@main` annotation. The main function can have
 * any name.
 *
 * To launch this main:
 *   - under IntelliJ IDEA, just click on the green arrow in the gutter
 *     just below, then click "Run".
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
 *   - Main function may have parameters. You can use the usual `args:
 *     Array[String]`, or use the new vararg syntax `args: String*`. It
 *     is also possible to have more specific argument types, like `ip:
 *     String, maxRetry: Int`. In this case, your program will fail if
 *     the second argument is not an integer.
 *   - It is yet possible to declare your main the same way you use to
 *     declare it in Scala 2.
 */
