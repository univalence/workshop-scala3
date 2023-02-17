package io.univalence.ws_scala3.internal

import scala.collection.mutable
import scala.util.Try

class Database private[internal]:
  private val data: mutable.Map[String, Table[_]] = mutable.Map.empty

  def getOrCreateTable[A](name: String): Table[A] =
    val table = data.get(name).map(_.asInstanceOf[Table[A]]).getOrElse(new Table[A])
    data(name) = table
    table

  def deleteTable(name: String): Unit = data.remove(name)

  def safeDeleteTable(name: String): Try[Unit] = Try(data.remove(name))

object Database:
  private[internal] lazy val locaDb: Database = new Database

  def connectLocal: Database = locaDb

class Table[A] private[internal]:
  private val data: mutable.TreeMap[String, A] = mutable.TreeMap.empty

  def put(key: String, value: A): Unit = data.update(key, value)

  def get(key: String): A = data(key)

  def safeGet(key: String): Option[A] = data.get(key)

  def remove(key: String): Unit = data.remove(key)

  def safeRemove(key: String): Try[Unit] =
    data
      .remove(key)
      .map(_ => ())
      .toRight(new NoSuchElementException("key not found: " + key))
      .toTry

  def getAll: Iterator[(String, A)] = data.iterator

  def getAllWithPrefix(prefix: String): Iterator[(String, A)] = data.iterator.filter((key, _) => key.startsWith(prefix))

  def getAllFrom(lowerBound: String): Iterator[(String, A)] = data.iteratorFrom(lowerBound)

  def getAllBetween(lowerBound: String, upperBound: String): Iterator[(String, A)] =
    data.range(lowerBound, upperBound).iterator
