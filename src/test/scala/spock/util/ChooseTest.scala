package spock.util

import org.scalatest.{FlatSpec, ShouldMatchers}

class ChooseTest extends FlatSpec with ShouldMatchers {

  "Chooser" should "pick the best element among weighted candidates" in {
    Choose.best(Map("a" -> 1d, "b" -> 2d, "c" -> 3d)) shouldBe "c"
  }

  it should "pick randomly from an collection of elements" in {
    val elems = 1 to 10
    for (_ <- 1 to 100) {
      Choose.randomly(elems) should (be >= 1 and be <= 10)
    }
  }

  it should "pick randomly from a percentile onwards" in {
    val elems = (1 to 100).zip(1 to 100 map (_.toDouble)).toMap
    for (_ <- 1 to 100) {
      Choose.randomlyOverPercentile(0.95, elems) should be >= 95
    }
  }
}
