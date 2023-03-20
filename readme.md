# Workshop Scala 3

The workshop in this project aims for you to discover some specific
features of Scala 3 language.

You can the Github repo with

```shell
$ git clone https://github.com/univalence/workshop-scala3
```

In this workshop, we will focus on:

* Toplevel main function
* Block delimitation
* Enum type
* Extension method
* Contextual abstraction and typeclass
* Selectable (structural types)
* Metaprogramming with inline

## Exercises

The exercises in this workshop are based on Scala 3 macros. You just
have to read the comments and launch main functions to see what you
have to do.

Once run, the output indicates you the section and exercise title, the
expressions to check inside each exercise and their result (error,
failure, or success). If the expression to check fails, the output
shows the line number in the file. If it has an error (it has thrown
an exception), you will see the line number of the wrong expression and
the exception (without the stacktrace).

Do not hesitate to use a debugger or to print intermediate results.

The macros defined are:

* `section` blocks (a group of exercises) with a title
* `exercise` blocks with a title and an activation flag
* `check` that contains a boolean expression and check it
* `!?` explicit placeholders for a type definition to complete
* `??` or `???` or `|>?` explicit placeholders for a missing implementation

If an exercise is not activated, you can turn its flag to `true`, once
you want to solve it.

## How to run

### Intellij

- Open the file you want to run (e.g `00-introduction.scala`)

- Near to the method with `@main` annotation, click on the green arrow to run the `main` method

- The output will be displayed in the `console`

### VS Code

- launch the terminal: `$> sbt`

- execute the project: `$> run`

- The following message `Multiple main classes detected. Select one to run:` will be printed. Choose the number of the main method you want to run (e.g: `1`).

- The output will be displayed in the `console`

## Topics not seen in this workshop

We will not see this other topics:

* Union type: ability to express that an element can be of one type or
  another (eg. `String | Null`)
* Intersection type: (eg. `A & B`)
* Opaque type: bringing type aliases without overhead
* Trait parameter
* Export
* Parameter untupling: now, you can write `l.foldLeft(0)((sum, v) => sum + v)`
* and many more...

## Dropped/deprecated features in Scala 3

Some features are deprecated and will be removed, or are already removed in Scala 3:

* Scala 2 macros -> inline and Scala 3 macros
* Do-while loop (no replacement)
* Procedure syntax (`def f() { ... }`) -> Use full function declaration syntax (`def f(): Unit = { ... }`)
* Package object -> Use top-level declaration
* Limit to 22, for number of parameters in functions and case classes, and for number of elements in tuples
* XML literals -> there is an XML string interpolation (`xml"<hello />"`)
* Symbol (no replacement)
* `private[this]` -> `private`
* `protected[this]` (no replacement)
* Wildcard Initializer (`var a: A = _`) -> `var a: A = scala.compiletime.uninitialized`
* and some more...

## Have fun!