package spock

import org.scalatest.{FlatSpec, ShouldMatchers}

class RangeTest extends FlatSpec with ShouldMatchers {

  "A range" should "have a lower bound actually not higher than the upper bound" in {
    an [IllegalArgumentException] shouldBe thrownBy {
      Range(2, 1)
    }
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
    Range(1, 100).splitBy(50) shouldBe (Some(Range(1, 49)), Some(Range(51, 100)))
    Range(1, 100).splitBy(1) shouldBe (None, Some(Range(2, 100)))
    Range(1, 100).splitBy(100) shouldBe (Some(Range(1, 99)), None)
  }
}
