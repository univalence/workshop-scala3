package io.univalence.ws_scala3

import io.univalence.ws_scala3.internal.exercise_tools._

object _01_ext {

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

  extension (o: Order) def total(): Double = o.items.foldLeft(0.0)((sum, item) => sum + item.quantity * item.unitPrice)

  class Record_(elems: (String, Any)*) extends Selectable {
    private val fields = elems.toMap

    def selectDynamic(name: String): Any = fields(name)
  }

  type Person =
    Record_ {
      val id: String
      val name: String
      val age: Int
    }

  @main
  def _01_Extension(): Unit =
    section("PART 1 - extension method") {
      val data =
        List(
          "id"   -> "123",
          "name" -> "Paul",
          "age"  -> 22
        )

      val p = Record_(data*).asInstanceOf[Person]

      println(p.name)
      println(p.selectDynamic("name"))
    }

}
