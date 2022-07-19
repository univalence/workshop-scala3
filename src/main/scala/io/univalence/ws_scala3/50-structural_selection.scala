package io.univalence.ws_scala3

import io.univalence.ws_scala3.internal.exercise_tools._

/**
 * =Structural selection=
 * Among the many secondary features appearing in Scala 3, the
 * ''structural selection'' is a one especially interesting for
 * developers who use to manipulate data.
 *
 * Structural selection is represented by the trait [[Selectable]]. For
 * example, it allows you to access a field of an instance by using the
 * dot accessor (eg. `anObject.a_field`) or by using index accessor like
 * a [[Map]] (ie. `anObject("a_field")`).
 *
 * This is done by implementing the trait [[Selectable]] and defining
 * the method `selectDynamic(name: String): Any`.
 */

object _01_structural_selection {

  /**
   * Let's define a type constructor. The fields and their value are
   * given with the parameter `elems`. This form of parameter makes then
   * easier to write down the field value.
   */
  class Data(elems: (String, Any)*) extends Selectable {

    /** `elems` is converted into a [[Map]]. */
    private val fields = elems.toMap

    /** This where the magic happens! */
    def selectDynamic(fieldName: String): Any = fields(fieldName)

    override def toString: String = elems.map((f, v) => s"$f=$v").mkString("{ ", ", ", " }")
  }

  /**
   * Let's define a type from the type constructor, representing ordered
   * items.
   */
  type OrderItem =
    Data {
      val productId: String
      val quantity: Int
      val unitPrice: Double
    }

  @main
  def _50_01_sel(): Unit =
    section("PART 1 - selectDynamic") {
      exercise("First approach", activated = true) {
        val orderItem = Data(
          "productId" -> "banana",
          "quantity"  -> 12,
          "unitPrice" -> 2.5
        ).asInstanceOf[OrderItem]

        println(s"order item: $orderItem")

        check(orderItem.quantity == ??)
        check(orderItem.selectDynamic("productId") == ??)
      }

      exercise("Read data from CSV", activated = true) {
        val data =
          """productId,quantity,unitPrice
            |banana,12,2.5
            |chocolate,5,2
            |coffee,10,1.6
            |""".stripMargin

        val rows = readCSVDataWithHeaders(data)

        val items =
          rows.map(fields =>
            Data(
              "productId" -> fields("productId"),
              "quantity"  -> fields("quantity").toInt,
              "unitPrice" -> fields("unitPrice").toDouble
            ).asInstanceOf[OrderItem]
          )

        check(items(0).quantity == ??)
        check(items(1).quantity == ??)
        check(items.map(item => item.quantity * item.unitPrice).sum == ??)
      }
    }

  def readCSVDataWithHeaders(csv: String): List[Map[String, String]] = {
    val lines   = csv.split("\n").toList.map(_.trim)
    val headers = lines.head.split(",").toList.map(_.trim)

    for {
      line <- lines.tail
      if line.nonEmpty
    } yield headers.zipAll(line.split(",").toList.map(_.trim), "", "").toMap
  }

}
