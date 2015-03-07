package spock

import scalaz.syntax.std.boolean._
import scala.collection.immutable

/** Bounds-included range */
case class Range(lower: Int, upper: Int) {
  require(lower <= upper, s"Invalid range $this")

  def size: Int = upper - lower + 1

  def contains(elem: Int): Boolean = elem >= lower && elem <= upper

  def iterable: immutable.Iterable[Int] = lower to upper

  def splitBy(pivot: Int): (Option[Range], Option[Range]) =
    (pivot > lower).option(Range(lower, pivot - 1)) ->
      (pivot < upper).option(Range(pivot + 1, upper))
}

object Range {
  val Initial = Range(1, 100)

  def apply(singleton: Int): Range = Range(singleton, singleton)
}
