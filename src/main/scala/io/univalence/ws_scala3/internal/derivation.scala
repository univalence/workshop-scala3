package io.univalence.ws_scala3.internal.derivation

import _root_.io.univalence.ws_scala3._03_typeclass.Hashed

/**
 * Automatic typeclass derivation for `Hashed` typeclass (see `40-contextual_abstraction.scala`).
 */
trait HashedDerivation {
  import scala.deriving.Mirror
  import scala.compiletime.{erasedValue, summonInline}

  inline def summonAll[A <: Tuple]: List[Hashed[_]] =
    inline erasedValue[A] match {
      case _: EmptyTuple => Nil
      case _: (t *: ts) => summonInline[Hashed[t]] :: summonAll[ts]
    }

  inline given derived[A](using m: Mirror.Of[A]): Hashed[A] = {
    lazy val instances = summonAll[m.MirroredElemTypes]
    inline m match {
      case s: Mirror.SumOf[A]     => deriveSum(s, instances)
      case p: Mirror.ProductOf[A] => deriveProduct(p, instances)
    }
  }

  private def deriveSum[A](s: Mirror.SumOf[A], instances: => List[Hashed[_]]): Hashed[A] =
    new Hashed[A] {
      extension (value: A) def hashed: Int = {
        val index = s.ordinal(value)
        instances(index)
          .asInstanceOf[Hashed[A]]
          .hashed(value)
      }
    }

  private def deriveProduct[A](p: Mirror.ProductOf[A], instances: => List[Hashed[_]]): Hashed[A] =
    new Hashed[A] {
      extension (value: A) def hashed: Int =
        value.asInstanceOf[Product].productIterator.zip(instances.iterator).map {
          case (field, instance) => instance.asInstanceOf[Hashed[Any]].hashed(field)
        }.sum
    }
}
