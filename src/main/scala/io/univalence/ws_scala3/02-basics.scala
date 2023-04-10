package io.univalence.ws_scala3

import io.univalence.ws_scala3.internal.exercise_tools.*
import scala.collection.immutable.LazyList.cons

/**
 * Before diving into the basics of the Scala language, we need to introduce the basics types
 * we have. Scala supports most of the basics type we already know and we are familiar with.
 */
@annotation.nowarn
def _02_00_types(): Unit = {
  section("PART 0 - Basic types") {

    exercise("Numeric types", activated = true) {
      /**
       * Scala supports commmon numeric types: Int, Long, Float, Double, BigDecimal.
       * These types are compatible with Java numeric types. Note that all the is no
       * primitives/object types in Scala. All these types are not represented by an
       * object at runtime. Scala will handle the unboxing.
       * 
       * The syntax `value: Type` is a type assertion. If the value is of this type
       * the compilation will succeed, and fails otherwise. This can also help if we
       * want to give a hint (during type inference).
       */
      3: Int
      3: Long
      3.0: Float
      3.0: Double
      3.0: BigDecimal

      // The compiler will convert the `Int` to `Double`for equality check. 
      check(3 == 3.0)

      // The can use a suffix with numeric type (i.e. `f` for float...)
      check(3L == 3f)
      check(3f == 3d)
    }

    exercise("Textual types", activated = true) {
      // Scala has a `Char` type (equivalent of the Java `Char` type).
      // There is no primitive type.
      'a': Char

      // We can convert Char to Int
      check('A'.toInt == ??)

      /**
       * The String type in Scala has all the methods of the underlying 
       * `java.lang.String`, of which it is just an alias. In addition, 
       * extension methods are added implicitly.
       * In scala a String can be converted to a list of `Char`.
       */
      "Hello": String

      // We can use Java String methods
      check("Hello".length() == ??)
    }

    exercise("Booleans", activated = true) {
      // These are the 2 booleans values we have in Scala.
      // At runtime booleans are represented as primitives types.
      true: Boolean

      false: Boolean

      // boolean operators are available
      check(!true == ??)
    }

    exercise("Unit type", activated = true) {
      // Unit type has one member named `()`
      val unitValue: Unit = ()

      check(unitValue == ??)

      // Unit is the return type of functions performing side effects (like I/O)
      println("We love Scala"): Unit

      check(println("We love Scala")  == ??)
    }

    exercise("Other specific scala types", activated = true) {
      /**
       * Scala has a `Null` type and its only value is `null`. Trying to call 
       * a method on the `null` value will lead to `NullPointerException` aka
       * the billion dollar mistake. Scala offer a way to handle possibly 
       * `null` values more easily. We will discuss it later.
       */
      null: Null

      /**
       * `null` can only be used with object types not numeric ones. The following
       * lines will fail to compile if we uncomment it
       */
      // var a: Int = null
      // check(3 == null)

      // We can compare `null` with object types like `String`.
      check("Hello" == null)
    }

  }
}

/**
 * =Scala basics=
 * In Scala, as in Java, we can declare variables, constants, functions and so on...
 * We will see how to do it in this section. What will be covered here:
 * - variables
 * - functions and methods
 * - collections
 * - conditionals
 * - for loops and expressions
 * - pattern-matching
 * Notions covered here are not exhaustive Scala functionnalities but what is
 * required to know for the next exercices. Beginners with the Scala language,
 * please do not skip this section.
 */
@main
def _02_01_variables(): Unit = {
  section("PART 1 - Variables") {

    /**
     * Scala has a `val` keyword for defining an immutable variable. The value
     * to assign to the variable could be an expression (like a computation or 
     * a function call), but the given expression will be evaluated and the 
     * returned value will be assigned to the variable.
     */
    exercise("A constant in Scala", activated = true) {
      // This is a constant (cannot be updated or redefined). It is not
      // mandatory to indicate the type, the compiler will infer it.
      val constant: Boolean = true
      
      /**
       * a compilation error with the message `Reassignment to val constant` will
       * be raised if we uncomment the following line.
       */
      // constant = false

      /**
       * The following line will raise the compilation error:
       * `constant is already defined as value constant`
       */
      // val constant = false

      // its value will be the same
      check(constant == ??)
    }

    /**
     * Scala also has a `var` keyword for defining a mutable variable. Like with
     * `val` the value assigned could also be an expression, and the value 
     * returned by the expression will be assigned to the variable.
     */
    exercise("A variable in Scala", activated = true) {
      // this is a variable (can be updated, cannot be redefined)
      var myVar: Int = 42

      check(myVar == ??)

      /**
       * The following line will raise the compilation error:
       * `myVar is already defined as variable myVar`
       */
      // var myVar = 33

      myVar += 10
  
      // its value can be updated
      check(myVar == ??)
    }

  }
}

/**
 * Like many languages we used to, Scala also support `if/else` 
 * block, but Scala has some specificity. Let's see if/else in Scala.
 */
@main
def _02_02_conditonals(): Unit = {
  section("PART 2 - Conditionals") {

    exercise("if/else", activated = true) {
      // We can define an if/else block very easily.
      // With the parenthesis, `then` is optional.
      // Note that `if/else` block returns a value.
      val isEven: Boolean = if (10 % 2 == 0) true else false
  
      check(isEven == ??)

      // If parenthesis are omitted, we need `then` after the condition.
      val isOdd: Boolean = if 10 % 2 != 0 then true else false
  
      check(isOdd == ??)
    }

    exercise("if/else if/else", activated = true) {
      val x = 0

      // we can have more than 2 branches.
      val message: String =
        if x < 0 then
          "negative"
        else if x == 0 then
          "zero"
        else
          "positive"
  
      check(message == ??)
    }

  }
}

/**
 * Now we will see how to define a function in Scala, and we will explore 
 * all the possibilities Scala's functions offers.
 */
@main
def _02_03_functions(): Unit = {
  section("PART 3 - Functions / Methods") {

    /**
     * Scala has the `def` keyword for defining a method, followed by the name
     * of that function, the parameters (with their names and their types) and 
     * the return type of the function. Please note that it is highly recommended
     * to specify the return (it could lead to compilation error).
     */
    exercise("A method", activated = true) {
      /**
       * method with name `double` with parameter named `value` of type `Int`, 
       * function return type (after `:`) is also `Int`. Note that function body
       * is defined after `=` (like with variables above ;-)).
       */
      def double(value: Int): Int = value * 2

      // Method could not be redefined. The following line will raise the compilation error:
      // `double is already defined as method double`
      // def double(value: Int): Int = value * 2

      // calling the method (parameters order count)
      check(double(11) == ??)

      // A method with many parameters
      def multiply(x: Int, y: Int): Int = x * y

      // calling the method with named parameters (parameters order does not count) 
      check(multiply(y = 3, x = 2) == ??)
    }

    exercise("A method without parameter", activated = true) {
      // This is a function without parameter
      def sameValue(): Int = 11

      // calling the method
      check(sameValue() == ??)

      // another function without parameter
      // Note that, it looks like a `val` ;-)
      def constant: Int = 42
  
      // calling the method
      check(constant == ??)
    }

    exercise("A recursive method", activated = true) {
      /**
       * Scala supports recursive methods. When defining a recursive method, the return type
       * of the method is mandatory otherwise the compilation will fail.
       * 
       * Scala is able to optimize natively a recursive function with the `@tailrec` annotation.
       * A prerequisite for that optimization will be to have the recursive call to be last
       * instruction to execute. Note that the `@tailrec` annotation is not mandatory when
       * defining a recursive function
       */
      @scala.annotation.tailrec 
      def length(value: String, acc: Int): Int =
        if (value == "") acc else length(value.tail, acc + 1)

      check(length("The champion is in the room", 0) == ??)

      /**
       * The following implementation will fail to compile if we uncomment the following line
       * with the error: `Cannot rewrite recursive call: it is not in tail positionbloop`.
       * If not adding the annotation, the function will compile and run successfully.
       */
      // @scala.annotation.tailrec
      def sum(num: Int): Int =
        if (num <= 1) 1 else sum(num - 1) + num

      check(sum(9) == ??)
    }

    exercise("A lambda function", activated = true) {
      /**
       * `Int => Int` is a lambda function type (it can be inferred).
       * We also use `=>` to implement that lambda function. Note that 
       * `x` parameter type is not mandatory (can be inferred when 
       * specifying `inc` variable type).
       * 
       * Also note that a lambda function can be assigned to a variable.
       * In Scala, we say that a function is a first class citizen (like
       * `Int`, `String`...).
       */
      val inc: Int => Int = (x: Int) => x + 1

      // calling the function in a regular way
      check(inc(42) == ??)
    }

    exercise("Returning a function", activated = true) {
      /**
       * A method can have multiple groups of parameters. The method will be
       * slightly different from the one we have seen above. Let's see that.
       */
      def withSpace(x: String)(y: String): String = x + " " + y

      // calling the method will all arguments
      check(withSpace("Hello")("World") == ??)

      /**
       * We can call it will one argument after another. When specifying the 1st argument,
       * we get a function with one parameter (the Znd of `withSpace`function).
       */
      val withHello: String => String = withSpace("Hello")

      // when calling the obtained function
      check(withHello("Folks") == ??)

      /**
       * We can achieve the same the lambda function syntax. Here `log` is a lambda function
       * needing two parameters of type `String`.
       */
      val log: String => String => String = source => level => s"$level - $source: some log message..."

      // when calling it with all the arguments
      check(log("io.univalence.ws_scala3._02_02_functions")("DEBUG") == ??)

      // we can have an intermediate function when passing the 1st argument
      val mainLog: String => String = log("io.univalence.ws_scala3.Main")

      // when calling the obtained function (it is same as before)
      check(mainLog("INFO") == ??)
      check(mainLog("ERROR") == ??)
    }

    exercise("Taking a function as parameter", activated = true) {
      /**
       * A method (or a function) can take a function as parameter.
       * Here `welcome` is expecting a function taking a `String` as
       * parameter and returning a `String`.
       */
      def welcome(f: String => String): String = f("Welcome")

      // When calling it, we need to pass a function
      check(welcome(prefix => s"$prefix Ryan") == ??)
    }

  }
}

/**
 * We will explore some of the Scala collections now (Array, Range, List, Map).
 * For a more in-depth overview of the scala collections, please refer to the 
 * official documentation.
 * 
 * Note that Scala collections are immutable (except `Array`).
 */
@main
def _02_04_collections(): Unit = {
  section("PART 4 - Collections") {

    exercise("Array", activated = true) {
      /**
       * Scala offers the `Array` type. Arrays are mutable, indexed collections of
       * values. `Array[T]` is Scala's representation for Java's `T[]` (`T` being
       * `String` in our example).
       */
      val z: Array[String] = Array("A", "B", "C")

      // It is possible to get an item by its index
      check(z(0) == ??)

      //all collections have the `head` method to get the 1st item in the collection
      check(z.head == ??)

      // We can test if the array is empty, isEmpty is supported on all collections.
      check(z.isEmpty == ??)

      // An array can be updated
      z(2) = "D"

      // if we check that value
      check(z(2) == ??)
    }

    exercise("Array from range", activated = true) {
      /**
        * An array can be created from a range. The starting value (`0`)
        * is included but the end (`5`) is excluded.
        */
      val arrayRange: Array[Int] = Array.range(0, 5)

      // Array size
      check(arrayRange.size == ??)

      // First item
      check(arrayRange(0) == ??)

      // Second item
      check(arrayRange(1) == ??)

      // Last item
      check(arrayRange.last == ??)
    }

    exercise("Array from range with higher step", activated = true) {
      /**
        * and we can even specify the step of the range
        */
      val arrayRange: Array[Int] = Array.range(0, 5, 2)

      // array size
      check(arrayRange.size == ??)

      // First item
      check(arrayRange(0) == ??)

      // Second item
      check(arrayRange(1) == ??)

      // Last item
      check(arrayRange.last == ??)
    }

    exercise("Simple range", activated = true) {
      /**
        * Scala has a range collection and has a specific api
        * to create a range object. By default, the default step 
        * is 1 and the end in included in the range.
        * The range is not evaluated yet (lazy).
        */
      val range: Range = 1 to 10

      // range size
      check(range.size == ??)

      // First item
      check(range.head == ??)

      // 2nd item
      check(range(1) == ??)

      // Last item
      check(range.last == ??)
    }

    exercise("Simple range with until syntax and step 2", activated = true) {
      /**
        * We can create a range with the outer border not included
        * with `until`. Here we also specify a wider step with `by`.
        */
      val range: Range = 1 until 10 by 2

      // range size
      check(range.size == ??)

      // First item
      check(range.head == ??)

      // 2nd item
      check(range(1) == ??)

      // Last item
      check(range.last == ??)
    }

    exercise("Numeric range with Char", activated = true) {
      import scala.collection.immutable.NumericRange

      /**
        * A range can be created with types other than numerical ones.
        * Here, we are choosing Char type for our example.
        */
      val range: NumericRange[Char] = 'a' to 'j' by 2

      // First item
      check(range.head == ??)

      // a range can be converted to an array. This will evaluate the range.
      check(range.toArray == ??)
    }

    exercise("List", activated = true) {
      /**
       * Scala has a List collection. This is how to declare a List in Scala.
       * Unlike Range, List are not lazy. 
       * List are immutable in Scala.
       */
      val fruit: List[String] = List("hello", "the", "world")

      check(fruit.isEmpty == ??)

      /**
       * other syntax to instanciate a List. Parenthesis are optional (here for clarity only).
       * A `List` in Scala can be seen as a head and a tail (which is the rest of the list).
       * You may have guessed it, the List in Scala is recursive datatype, `Nil` being the
       * empty List.
       */
      val nums: List[Int] = 1 :: (2 :: (3 :: (4 :: Nil)))
      val emptyList: List[Int] = Nil

      check(nums.isEmpty == ??)
      check(emptyList.isEmpty == ??)

      // first item, calling head on an empty list will throw an exception.
      check(nums.head == ??)

      // The rest of the list, we also get an exception if list is empty.
      check(nums.tail == ??)
    }

    exercise("Seq", activated = true) {
      /**
       * `Seq` is the supertype of sequence collections like `Range` and `List`.
       * Scala's `Seq` are immutable.
       */
      val fruit: Seq[String] = Seq("See", "you", "soon")

      check(fruit.isEmpty == ??)

      // first item
      check(fruit.head == ??)
    }

    exercise("Set", activated = true) {
      /**
       * We also have the type `Set` in Scala.
       * This is the empty `Set` of type `Int`.
       */
      var s : Set[Int] = Set.empty // or Set()

      // An non empty Set
      val fruit = Set("apple", "orange", "peach", "banana")

      // we can to check if an element is in the Set
      check(fruit("peach") == ??)

      // if we check with an element not in the Set
      check(fruit("potato") == ??)

      // if we add an item not in the set
      val fruitPlusMelon = fruit + ("melon")

      check(fruitPlusMelon == ??)

      // if we add an item already in the set
      val sameFruit = fruit + ("banana")
      
      check(sameFruit == ??)
    }

    exercise("Map", activated = true) {
      /**
       * We also have the type `Map` in Scala. A Map in Scala could be seen
       * as a list of tuple (key/value pairs). This is the empty `Map`.
       */
      val m: Map[Char,Int] = Map() // or Map.empty

      check(m.size == ??)

      /**
       * A non empty map, we need to pass a list of tuple values (key/value pair).
       * `->` in Scala create a `Tuple` object.
       */ 
      val colors = Map("red" -> "#FF0000", "azure" -> "#F0FFFF")

      // To get the value of key `red`, we get an error if key is missing
      check(colors("red") == ??)

      // We can test if key is contained
      check(colors.contains("black") == ??)

      // to add an item, we pass the pair to add to the map
      val colorsWithBlack = colors + ("black" -> "#FFFFFF")

      // if we redo the check
      check(colors.contains("black") == ??)
    }

    /**
     * Scala offers an `Option` type to handle possibly `null` values.
     * The `Option` type is integrated with collections seen previously.
     * We can think about `Option` as a container with a value or no
     * value if we have `null`, so that we can call any method on `Option`
     * whithout having to check about if the value is missing.
     */
    exercise("Option", activated = true) {
      // We just put a value in the container for a non `null` value
      val intOption: Option[Int] = Some(3)

      // we can get the value inside that `Option`, it will throw an
      // NullPointerException if it's `None`.
      check(intOption.get == ??)

      // Other methods are available on `Option`
      check(intOption.isDefined == ??)
      check(intOption.isEmpty == ??)

      // When there is no value, we can use `None`. `None` is an `Option``
      // but there is no value inside it.
      val nullInt: Option[Int] = None

      // We can give a default value if we have a `None`.
      check(nullInt.getOrElse(0) == ??)
      check(nullInt.isDefined == ??)
      check(nullInt.isEmpty == ??)
    }

    exercise("Option with other collections", activated = true) {
      val helloMaybe = Some("Hello")

      // we can get the size of an `Option`
      check(helloMaybe.size == ??)

      // we can get a List from an `Option`, we get the empty list for `None`
      check(helloMaybe.toList == ??)

      // We can get the head of a List as an `Option` to avoid an exception
      check(List(1, 2, 3).headOption == ??)
      check(List(1, 2, 3).lastOption == ??)

      // We can also get a value from a Map as an `Option` which avoid an exception
      check(Map(1 -> "Hello").get(1) == ??)

      // In the other way, from an `Option` we can get a `Map`
      check(Option(1 -> "Yes").toMap == ??)
    }

  }
}

/**
 * Scala has a `for` loop has in  many language, but it differ in many ways.
 * We will explore Scala `for` control structure over the collections we have
 * seen before.
 */
@main
def _02_05_for(): Unit = {
  section("PART 5 - For loops and expressions") {

    /**
     * We can achieve very easily a `for` loop like this.
     */
    exercise("for loop", activated = true) {
      // the accumulator
      var result = 0

      /**
       * The `for` loop will iterate over the list, we do
       * not need to declare the iterator variable `i`.
       * `i` only exist in this scope and is not available
       * elsewhere.
       */
      for i <- List(1, 2, 3, 4) do result += i

      check(result == ??)
    }

    exercise("for loop with guards", activated = true) {
      var result = 0

      /**
       * The `for` loop will also iterate over the list, here we
       * can filter value during the iteration, we call it a 
       * `guard`. We need a condition returning a boolean for the
       * guard. Note that parenthesis are not required for the
       * condition.
       */
      for i <- List(2, 3, 4, 5, 6) if i > 2 do result += i

      check(result == ??)
    }

    exercise("double for loop", activated = true) {
      var result = 0

      /**
       * We can iterate over multiple collections with a for loop.
       * This is similar to two for loops.
       */
      for
        i <- List(1, 2, 3)
        j <- List(5, 6, 7)
        if i == 2
        if j == 6
      do
        result += i * j

      check(result == ??)
    }

    exercise("for expressions", activated = true) {
      // The list on which we want to iterate
      val ints = List(4, 5, 6, 7)

      /**
       * `for` in Scala can return a value, we call it `for-comprehension.
       * Here we iterate on the list and double each element. With the `yield`
       * keyword, the value returned at each step of the iteration will replaced
       * the element of the list at the corresponding index. As `List` are
       * immutables, a new `List` will be returned.
       * 
       * This is similar to using the `map` on `List`.
       */
      val result = for i <- ints yield i * 2

      check(result == ??)
    }

    exercise("for expressions on 2 lists", activated = true) {
      // The lists on which we want to iterate
      val evens = List(2, 4, 6, 8)
      val odds = List(1, 3, 5, 7)

      /**
       * Here we want to iterate over 2 lists and compute a new value for each
       * combination of each value of the two lists. As we iterating over 2 lists,
       * the list returned will be concat to the previously computed list.
       * 
       * This is similar to using `flatMap`and `map` on `List`.
       */
      val result = for 
        i <- evens
        j <- odds
      yield i + j

      check(result.size == ??)

      check(result == ??)
    }

    exercise("for expressions on 2 lists", activated = true) {
      /**
       * It is also possible to use a `for-comprehension` with `Option`. The value
       * inside the container will be extracted and then put back into it.
       */
      val result = for {
        start <- Some("hello")
        end <- Some(".")
      } yield s"$start buddy, take a seat and a coffee $end"

      check(result == ??)
    }

  }
}
/**
 * Scala's pattern-matching is one of its most famous features. This section will
 * introduce it with what we have seen until now. Pattern-matching will also be
 * discussed later when talking about Scala OOP features. Let's dive now into
 * pattern-matching.
 */
@main
def _02_06_match(): Unit = {
  section("PART 6 - Match expressions") {

    exercise("Basic match", activated = true) {
      /**
       * This is the basic syntax for the pattern-matching in Scala.
       * In this example, it is very similar to a `switch`. `case`
       * keyword introduce each case we want to handle. Once a case
       * is matched, a value is returned and the pattern-matching
       * is ended. We use `_` for the default case. If we omit the
       * default case the compiler will raise a warning.
       * 
       * Note that, like `if` or `for`, the pattern-matching returns
       * a value in Scala.
       */
      def text(value: Int): String = value match {
        case 1 => "one"
        case 2 => "two"
        case _ => "other"
      }

      check(text(2) == ??)

      check(text(5) == ??)
    }

    exercise("Pattern matching on Any", activated = true) {
      /**
       * With pattern-matching, we can match on values of different types.
       * `Any` is the supertype of all types in Scala (Int, String, custom types...).
       * We match on specific values with the 2 first cases, then we match on all
       * values of type `Int`. Note that order is important, the specific case
       * here needs to be before more general one.
       * 
       * When matching on all values of type `Int` (third case), the value matched
       * is assigned into the variable `y`, that variable is declared on the fly and
       * only exists in this scope (this case) and not elsewhere.
       * 
       * Note that a default case is highly recommended, the compiler will warn us if
       * we forget the default case.
       */
      def matchTest(x: Any): Any = x match {
        case 1 => "One"
        case "two" => 2
        case y: Int => "Int Value"
        case _ => "Many"
      }

      check(matchTest("two") == ??)

      check(matchTest(7) == ??)

      check(matchTest("test") == ??)
    }

    exercise("Pattern matching on List", activated = true) {
      /**
       * We pattern-match on a list in a similar way we construct it.
       * When the list is not empty, we assigned the first item of the
       * list into the `head` variable (declared on the fly and available
       * only in this scope) and the rest of the list in in the `rest`
       * variable (remember list is a recursive datatype).
       * 
       * Note that the compiler deduces here that the pattern-matching is 
       * exhaustive (as we only have two cases), so a default case `_`
       * is not required.
       */
      def listToString(list: List[String]): String = list match {
        case head :: rest => s"$head ${listToString(rest)}"
        case Nil => ""
      }

      check(listToString("have" :: "a" :: "nice" :: "day" :: Nil) == ??)

      check(listToString("pizza" :: "or" :: "burritos" :: "?" :: Nil) == ??)
    }

    exercise("Pattern matching on Option", activated = true) {
      /**
       * We can also do a pattern-matching on an `Option`. Just like `Option`,
       * we only have to check the two subtypes of `Option` (`Some` and `None`).
       * On this example, when matching on `Some` we extract the value inside it
       * and assign it to the variable `name`(scoped to that branch of the 
       * pattern-matching).
       */
      def whatToEat(value: Option[String]): String = value match {
        case Some(name) =>
          s"$name - enjoy :-)"
        case None =>
          "Nothing to eat :-("
      }

      val garden = Map(1 -> "Pizza", 2 -> "Salad")

      check(whatToEat(garden.get(1)) == ??)

      check(whatToEat(garden.get(3)) == ??)
    }

  }
}
