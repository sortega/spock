package spock

import org.scalatest.{FlatSpec, ShouldMatchers}

class RangeTest extends FlatSpec with ShouldMatchers {

  "A range" should "can be empty or not" in {
    Range(2, 1) shouldBe Range.Empty
    Range(1, 2) shouldBe Range.NonEmpty(1, 2)
  }

  it should "be defined as a singleton range" in {
    Range(13) shouldBe Range(13, 13)
  }

  it should "have a size" in {
    Range(10).size shouldBe 1
    Range(1, 20).size shouldBe 20
  }

  it should "check for inclusion" in {
    Range(1, 10).contains(0) shouldBe false
    Range(1, 10).contains(1) shouldBe true
    Range(1, 10).contains(5) shouldBe true
    Range(1, 10).contains(10) shouldBe true
    Range(1, 10).contains(11) shouldBe false
  }

  it should "be iterable" in {
    Range(1, 5).iterable.toList shouldBe List(1, 2, 3, 4, 5)
  }

  it should "be split by a pivot" in {
    Range(1, 100).splitBy(50) shouldBe (Range(1, 49), Range(51, 100))
    Range(1, 100).splitBy(1) shouldBe (Range.Empty, Range(2, 100))
    Range(1, 100).splitBy(100) shouldBe (Range(1, 99), Range.Empty)
  }

  it should "be intersected" in {
    Range(1, 10) intersect Range(2, 8) shouldBe Range(2, 8)
    Range(1, 6) intersect Range(2, 8) shouldBe Range(2, 6)
    Range(1, 2) intersect Range(8, 10) shouldBe Range.Empty
    Range.Empty intersect Range(1, 2) shouldBe Range.Empty
    Range(1, 2) intersect Range.Empty shouldBe Range.Empty
  }
}
