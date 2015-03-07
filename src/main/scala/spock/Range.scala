package spock

import scala.collection.immutable

sealed trait Range {
  def size: Int
  def contains(elem: Int): Boolean
  def iterable: immutable.Iterable[Int]
  def splitBy(pivot: Int): (Range, Range)
  def intersect(other: Range): Range
}

object Range {
  val Initial = NonEmpty(1, 100)

  def apply(lower: Int, upper: Int): Range =
    if (lower <= upper) NonEmpty(lower, upper) else Empty

  def apply(singleton: Int): NonEmpty = NonEmpty(singleton, singleton)

  case object Empty extends Range {
    override def size = 0
    override def iterable: scala.collection.immutable.Iterable[Int] = 1 to 0
    override def splitBy(pivot: Int) = (Empty, Empty)
    override def contains(elem: Int) = false
    override def intersect(other: Range) = Empty
  }

  /** Bounds-included range */
  case class NonEmpty(lower: Int, upper: Int) extends Range {
    require(lower <= upper, s"Invalid range $this")

    override def size: Int = upper - lower + 1

    override def contains(elem: Int): Boolean = elem >= lower && elem <= upper

    override def iterable: immutable.Iterable[Int] = lower to upper

    override def splitBy(pivot: Int) = Range(lower, pivot - 1) -> Range(pivot + 1, upper)

    override def intersect(other: Range) = other match {
      case Empty => Empty
      case otherRange: NonEmpty => Range(lower max otherRange.lower, upper min otherRange.upper)
    }
  }
}
