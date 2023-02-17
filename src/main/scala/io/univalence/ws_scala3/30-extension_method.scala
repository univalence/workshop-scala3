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

import io.univalence.ws_scala3.internal.exercise_tools._

/**
 * =Extension methods=
 * An '''extension method''' is a method that is added by a developer in
 * a specific scope to an existing type outside of this type
 * declaration. It is useful especially when you want to add behaviors
 * to class that you are not allowed to modify.
 *
 * Extension methods already existed in Scala 2, but you have to declare
 * an implicit class to add methods. In Scala 3, you benefit of a
 * specific syntax.
 */
object _01_extension_method {

  /**
   * We propose to represent client orders to a store. An order is
   * composed of a proper ID, the store ID, and a list of bought items.
   * Each item contains the ID of the product bought, its quantity, and
   * its unit price.
   *
   * We will suppose that those case classes are automatically generated
   * from a schema (eg. protobuf, avro...). So, we cannot modify them to
   * add methods, because they will disappear at the next regeneration.
   */

  case class OrderItem(
      productId: String,
      quantity:  Int,
      unitPrice: Double
  )

  case class Order(
      orderId: String,
      storeId: String,
      items:   List[OrderItem]
  )

  @main
  def _30_01_order_extension(): Unit =
    section("PART 1 - extension method on order") {

      /**
       * By this extension method, we add a behavior to Order in a view
       * to get the total price of an order.
       *
       * The declaration starts with a parameter representing the order
       * on which the method will be added. Then, we can declare the
       * method that will be added to the order.
       */
      extension (o: Order)
        def total(): Double = o.items.foldLeft(0.0)((sum, item) => sum + item.quantity * item.unitPrice)

      val order =
        Order(
          orderId = "123",
          storeId = "Plouzané",
          items =
            List(
              OrderItem(productId = "chocolat", quantity = 2, unitPrice  = 2.0),
              OrderItem(productId = "café", quantity     = 10, unitPrice = 3.0),
              OrderItem(productId = "pomme", quantity    = 6, unitPrice  = 3.0)
            )
        )

      exercise("What will be the total price?", activated = true) {
        comment("Waht is the total price of the order?")
        check(order.total() == ??)
      }

      exercise("Your turn! Count items in an order", activated = false) {
        // `countProducts` is a method that should count the number of
        // products that appears in an order/

        // TODO uncomment the line below and declare the extension method `countProducts`
//        check(order.countProducts() == 18)
      }
    }

}

/**
 * It is possible to add many extension methods to a type in one
 * declaration.
 *
 * Here is the syntax for this feature:
 * {{{
 *   extension (value: T) {
 *     def m1 = ???
 *     def m2 = ???
 *   }
 * }}}
 *
 * Here again, you can use the different syntaxes. Like the Python one:
 * {{{
 *   extension (value: T):
 *     def m1 = ???
 *     def m2 = ???
 * }}}
 *
 * Or the Ruby one:
 * {{{
 *   extension (value: T)
 *     def m1 = ???
 *     def m2 = ???
 *   end extension
 * }}}
 */
object _02_multi_extension {

  extension (s: List[String]) {
    def smallest: String = s.minBy(_.length)

    def largest: String = s.maxBy(_.length)

    def padToLargest: List[String] = {
      val largestSize = s.largest.length

      s.map(_.padTo(largestSize, ' '))
    }
  }

  @main
  def _30_02_add_multi_extension(): Unit =
    section("Add multiple extension methods") {
      exercise("Multiple extension method", activated = true) {
        val l =
          List(
            "genoux",
            "choux",
            "cailloux",
            "hiboux"
          )

        comment("What is the smallest word in the list?")
        check(l.smallest == ??)
        comment("What is the largest word in the list?")
        check(l.largest == ??)
        comment("What is the list words right padded according to the largest word?")
        check(l.padToLargest == ??)
      }
    }

}
