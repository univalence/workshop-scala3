package io.univalence.ws_scala3

import io.univalence.ws_scala3.internal.exercise_tools.*

/**
 * Scala try to unified functional programming and OOP. This set of exercices is focused on
 * class on the Scala language. How to define a class ? How to create instances ? Are they
 * different from other languages ? We will try to cover these aspects of Scala's class in
 * this section.
 */
@main
def _03_01_class(): Unit = {
  section("PART 1 - Classes") {

    exercise("Class introduction", activated = true) {
      /**
       * This is how we define a simple class. The class `Greeter` has two attributes:
       * `prefix` et `suffix`. Did you noticed we did not define a constructor method.
       * The class declaration creates a constructor.
       */
      class Greeter(prefix: String, suffix: String) {
        def greet(name: String): String =
          // `this` can be used inside class (but it is not mandatory).
          this.prefix + name + suffix
      }

      // `new` is not needed for instanciation, we could also use named parameters.
      val greeter = Greeter(prefix = "Salut ", suffix = ", bonne journée")

      // Once we have an instance, we can call the `greet` method.
      check(greeter.greet("Martin") == ??)
    }

    exercise("Accessing class attribute", activated = true) {
      /**
       * Only `prefix` attribute will have a getter since we define it as a `val`.
       * On the contrary, suffix will not have any getter or setter. It will be 
       * possible to use it only within the class.
       */
      class Greeter(val prefix: String, suffix: String) {
        def greet(name: String): String =
          // suffix can only be used here (inside Greeter)
          this.prefix + name + suffix
      }

      val greeter = Greeter(prefix = "Salut ", suffix = ", bonne journée")
  
      // We are accessing `prefix` it through the getter.
      check(greeter.prefix == ??)
    }

    exercise("Class with variables", activated = true) {
      /**
       * Here `suffix` attribute will have a getter and a setter since it is marked
       * as a `var`.
       */
      class Greeter(prefix: String, var suffix: String) {
        def greet(name: String): Unit =
          println(this.prefix + name + suffix)
      }

      val greeter = Greeter(prefix = "Salut ", suffix = ", bonne journée")

      // We are accessing `suffix` it through the getter.
      check(greeter.suffix == ??)

      // We could modify it with the setter
      greeter.suffix = ", bonne soirée"
  
      // When checking the value again
      check(greeter.suffix == ??)
    }

  }
}

/**
 * We will talk about the singleton design pattern in Scala in this section.
 */
@main
def _03_02_singleton(): Unit = {
  section("PART 2 - Singleton") {

    exercise("Simple singleton", activated = true) {
      /**
        * Scala offers the singleton design pattern as a language construct, i.e. the 
        * language offers a syntax to create a singleton object. In this example, to
        * define a singleton, we can just declare it with the `object` keyword. A 
        * singleton is lazily loaded (when used, the instance will be created).
        */
      object StringUtils {
        def leftPad(str: String, size: Int, padChar: Char): String =
          (1 to (size - str.size)).map(_ => padChar).mkString ++ str
      }

      /**
        * We do not need to instanciate the singleton before using it. As we want to 
        * access `leftPad` method, the singleton will be instanciated.
        */
      check(StringUtils.leftPad("Obiwan", 10, '.') == ??)

      check(StringUtils.leftPad("Hello", 8, '_') == ??)
    }

    exercise("Companion object", activated = true) {
      /**
        * Scala does not support static method or class. We can use
        * a singleton instead.
        */
      class Square(side: Double) {
        import Square._
        def area: Double = calculateArea(side)
      }

      /**
        * A object with the same as a class is called a **Companion object**.
        * It can access private fields from a class, and inversely the class
        * can access any private field of the companion object.
        */
      object Square {
        private def calculateArea(side: Double): Double = side * side
      }

      val square = Square(4.0)

      check(square.area == ??)
    }

    exercise("Companion object with apply method", activated = true) {
      /**
       * A class could have a companion object which define a `apply` method.
       * Scala treats `apply` method in a particular way. Let's see it with
       * an example.
       * Here we have the standard constructor the class.
       */
      class Greeter(prefix: String, suffix: String) {
        def greet(name: String): String =
          s"$prefix $name $suffix"
      }

      object Greeter {
        /**
         * Could be seen as a factory method (to create a `Greeter`).
         * Here we want to define a convenient method to create a 
         * `Greeter` which allows prefix and suffix as a single string.
         */
        def apply(prefixAndSuffix: String): Greeter = {
          // prefix and suffix are separated with a `;`, we split it
          // and deconstruct the array to extract the values
          val Array(prefix, suffix) = prefixAndSuffix.split(";")
          // We use `new` keyword to avoid an ambiguity (will fail to compile).
          new Greeter(prefix, suffix)
        }
      }

      /**
       * We are using the `apply`method, when calling the `Greeter` like this the
       * compiler will automatically call the `apply` method under the hood.
       */
      check(Greeter("Hi; Ciao.") == ??)
    }

  }

}


/**
 * A case class has all of the functionality of a class, and also has additional
 * features baked in that make them useful for functional programming. When the
 * compiler sees the case keyword in front of a class it enrich it with some
 * functionalities we will see in this section.
 */
@main
def _03_03_case_class(): Unit = {
  section("PART 3 - Case class") {

    /**
     * We define case class like regular classes. However, using `case` keyword
     * have many benefits.
     */
    case class Person(name: String, age: Int)

    exercise("Case class introduction", activated = true) {
      // Like before we can instanciate the class without `new`
      val martin = Person("Martin", 11)

      // getters are generated for all attributes
      check(martin.name == ??)
      check(martin.age == ??)

      /**
       * The following will fail to compile because the compiler does not generate
       * any setter. `case` classes are immutable. It can be seen as a class
       * representing a value so the language does not give the possibility
       * to mutate the class state.
       */
      // martin.name = "Bob"
    }

    exercise("Case class copy", activated = true) {
      val martin = Person("Martin", 11)

      /**
       * As seen before, the class is immutable. We have the `copy` method which
       * can update an attribute of the class. Note that we get a new instance,
       * so the initial one is not modified.
       */
      val otherMartin = martin.copy(age = 16)

      check(martin.name == ??)

      check(martin.age == ??)
    }

    exercise("Case class equals", activated = true) {
      /**
       * As said before, a `case` class represents a value. So the compiler
       * automatically generates `equals` and `hashcode` methods so that classes
       * comparison will be on values, not references.
       */
      val martin = Person("Martin", 11)
      val otherMartin = Person("Martin", 11)
      val alonzo = Person("Alonzo", 12)

      val isMartinsEquals = martin == otherMartin

      val isMartinEqualsAlonzo = martin == alonzo

      check(isMartinsEquals == ??)

      check(isMartinEqualsAlonzo == ??)

      // Also note that a default `toString` is also generated
      check(martin.toString() == ??)
    }

    exercise("Case class and pattern-matching", activated = true) {
      /**
       * `greeting` method takes a `Person` as parameter and pattern-match on
       * it for its implementation. We can pattern-match on a `case class` in
       * a similar way as we instanciate it, attributes are automatically
       * extracted into variables `name` and `age`. The last `case` is the
       * default.
       */
      def greeting(person: Person): String =
        person match {
          case Person(name, age) if age < 18 => s"Hello young person $name"

          case Person(name, age) if age > 60 => s"Hello grandparent $name"

          case p => s"Hello adult ${p.name}"
        }

      check(greeting(Person("Alan", 22)) == ??)

      check(greeting(Person("Phillip", 14)) == ??)

      check(greeting(Person("Ada", 66)) == ??)
    }

  }
}

/**
 * Scala supports abstract classes. They are very similar to Java ones.
 */
@main
def _03_04_abstract_class(): Unit = {
  section("PART 4 - Abstract class") {

    exercise("Simple abstract class", activated = true) {
      // We just have to use `abstract` keyword (like in Java).
      // Abstract classes can define method which will be inherited.
      abstract class Topping {
        def description: String = "Pepperoni"
      }

      // A class can extends from only abstract class
      class Pepperoni() extends Topping {}

      val top = Pepperoni()

      // method from abstract class is available
      check(top.description == ??)
    }

    exercise("Abstract class with params", activated = true) {
      /**
       * Like regular classes, abstract classes can take attributes, here `name`
       * will be available only within the `Topping`.
       */
      abstract class Topping(name: String) {
        def description: String = s"topping name: $name"
      }

      // We need to pass the attribute of the abstract class (like a function).
      class Pepperoni(name: String) extends Topping(name) {}

      val top = Pepperoni("bridou")

      // method from abstract class is available
      check(top.description == ??)
    }

  }
}

/**
 * Scala traits can be used as simple interfaces, but they can also contain
 * abstract and concrete methods and fields, and they can have parameters, just
 * like classes. They provide a great way for you to organize behaviors into
 * small, modular units.
 */
@main
def _03_05_trait(): Unit = {
  section("PART 5 - Traits") {

    /**
     * This is how to create a trait. Methods defined in a trait can have
     * an implementation (but they can also be abstract).
     * 
     * This trait defines a greeting behaviour.
     */
    trait Greeter {
      def greet(name: String): String =
        s"Hello, $name !"
    }

    exercise("Simple trait", activated = true) {
      /**
       * We can instanciate a trait, if it has any abstract method we must
       * implement them.
       */
      val greeter = new Greeter {}

      check(greeter.greet("Fred") == ??)
    }

    exercise("Extending a trait", activated = true) {
      // We define a trait for the farewell behaviour.
      trait Bye {
        def ciao(name: String): String =
          s"Ciao $name, see you soon !"
      }

      /**
       * A class can extends several traits. We must use the `extends` keyword
       * for the 1st trait and `with` keyword with the others.
       */
      class DefaultGreeter() extends Greeter with Bye

      val greeter = DefaultGreeter()

      // we can call methods from the extended traits.
      check(greeter.greet("Fred") == ??)

      check(greeter.ciao("Fred") == ??)
    }

    exercise("Extending a trait when instanciating a class", activated = true) {
      // We have a class that does not extends any trait.
      class NoGreeter()

      /**
       * It is possible to extend from a trait when instanciating an object.
       * This is useful especially for classes we can change (from librairies
       * for example).
       */
      val greeter = new NoGreeter() with Greeter

      // `greet` method will be available (only for this instance)
      check(greeter.greet("Fred") == ??)
    }

    exercise("Trait and diamond inheritance problem", activated = true) {
      /**
       * Since we can have multiple inheritance with traits. Scala has a
       * mecanism to avoid the diamond problem called **Linearization**.
       */
      trait A {
        // trait `A` defines a method `foo`
        def foo = "A"
      }

      trait B extends A {
        // trait `B` overrides `A#foo`
        override def foo = "B" + super.foo
      }

      trait C extends A {
        // trait `C` also overrides `A#foo`
        override def foo = "C" + super.foo
      }

      /**
       * We want `D` to extends from C and B which overrides each the `foo`
       * method. The result will be the same if we extend on the fly.
       * 
       * The order of the extends is important. Scala will reorder the class
       * hierarchy as follow: D -> B -> C -> A (the last trait will be the
       * direct supertype).
       */
      class D extends  C with B

      // When calling foo, we get a result
      val d = new D
      check(d.foo == ??)
    }

  }
}

/**
 * Scala offers a different way (other than `final`) to limit inheritance and
 * subtyping. It achieve this with the `sealed` keyword. We will discuss it now
 * we are familiar with `case` classes and traits.
 */
@main
def _03_06_sealed(): Unit = {
  section("PART 6 - Sealed trait") {

    /**
     * We can define a hierarchy like this with a supertype `Person` and 2
     * subtypes. Note that `Person` is defined with the keyword `sealed`.
     * `sealed` ensure that all classes (singletons, trait...) that extends
     * from `Person` are in the same file. If not, we get a compilation error.
     * So it will not be possible for a user to extends the class hierarchy of
     * a library he is using for example.
     */
    sealed trait Person(val id: Int)

    // We need `override` keyword to indicate this `id` is overriding attribute from parent
    case class Employee(override val id: Int, name: String, managerId: Int) extends Person(id)

    case class Manager(override val id: Int, name: String) extends Person(id)

    exercise("Sealed trait example", activated = true) {
      // We can instantiate subtypes and treat them as instances of `Person`
      val employee: Person = Employee(1, "Fred", 2)

      val manager: Person = Manager(2, "Nelson")

      // `id` attribute will be available (exposed by parent)
      check(employee.id == ??)

      check(manager.id == ??)
    }

    exercise("Sealed and pattern matching", activated = true) {
      /**
       * When using a pettern-matching on a sealed hierachy, the compiler could
       * check that it is exhaustive. If a case is missing a warning will be
       * raised by a compiler.
       * 
       * Once you have see the warning, uncomment the last case and see if it
       * fix it.
       */
      def describe(p: Person): String = p match {
        case Employee(id, name, managerId) => s"Employee, id: $id, name: $name, manager id: $managerId"
        // case Manager(id, name) => s"Manager, id: $id, name: $name"
      }

      check(describe(Employee(1, "Fred", 2)) == ??)

      check(describe(Manager(2, "Nelson")) == ??)
    }

  }
}
