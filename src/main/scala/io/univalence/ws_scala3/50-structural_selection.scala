package io.univalence.ws_scala3

import io.univalence.ws_scala3.internal.exercise_tools._

/**
 * =Structural selection=
 *
 */

object _01_structural_selection {

  class Data(elems: (String, Any)*) extends Selectable {
    private val fields = elems.toMap

    def selectDynamic(fieldName: String): Any = fields(fieldName)

    override def toString: String = elems.map((f, v) => s"$f=$v").mkString("{ ", ", ", " }")
  }

  type OrderItem =
    Data {
      val productId: String
      val quantity: Int
      val unitPrice: Double
    }

  @main
  def _01_sel(): Unit =
    section("PART 1 - ???") {
      val line = "productId=banana,quantity=12,unitPrice=2.50"

      val values = lineToOrderItemValue(line)

      val item = Data(values.toList*).asInstanceOf[OrderItem]

      println(item)

      check(item.quantity == ??)
      check(item.selectDynamic("productId") == ??)

      exercise("Create your own order item", activated = false) {
        val itemBis: OrderItem = ???

        check(itemBis.toString == "{ productId=coffee, quantity=20, unitPrice=0.5 }")
      }
    }

  def lineToOrderItemValue(line: String): Map[String, Any] = {
    val values =
      line
        .split(",")
        .toList
        .map { s =>
          val elems = s.split("=", 2)
          (elems(0), elems(1))
        }

    Map(
      values(0),
      values(1)._1 -> values(1)._2.toInt,
      values(2)._1 -> values(2)._2.toDouble
    )
  }

}
