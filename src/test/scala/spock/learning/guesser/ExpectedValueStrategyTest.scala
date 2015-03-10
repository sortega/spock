package spock.learning.guesser

import org.scalatest.{FlatSpec, ShouldMatchers}
import spock.Range

class ExpectedValueStrategyTest extends FlatSpec with ShouldMatchers {
  val uniformStrategy = new ExpectedValueStrategy(PickerDistro.uniform((1 to 100).toSet))

  "The expected value strategy" should "choose the only option" in {
    uniformStrategy.choose(attempt = 1, range = Range(1)) shouldBe 1
    uniformStrategy.choose(attempt = 1, range = Range(50)) shouldBe 50
  }

  it should "choose the most likely out of two options" in {
    val strategy = new ExpectedValueStrategy(PickerDistro(1 -> 0.4, 2 -> 0.6))
    strategy.choose(attempt = 1, range = Range.NonEmpty(1, 2)) shouldBe 2
  }

  it should "choose the central elements when having an uniform distribution" in {
    val startTime = System.currentTimeMillis()
    uniformStrategy.choose(attempt = 1, range = Range.Initial) should equal (50 +- 35)
    uniformStrategy.choose(attempt = 2, range = Range.NonEmpty(1, 49)) should equal (25 +- 15)
    uniformStrategy.choose(attempt = 3, range = Range.NonEmpty(51, 74)) should equal (62 +- 10)
    uniformStrategy.choose(attempt = 1, range = Range.NonEmpty(1, 5)) should equal (3 +- 1)
    val millis = System.currentTimeMillis() - startTime
    millis should be < 2000L
  }

  it should "focus on the elements with most probability" in {
    val strategy = new ExpectedValueStrategy(PickerDistro.uniform((90 to 100).toSet))
    strategy.choose(attempt = 4, range = Range.Initial) should equal (95 +- 5)
  }
}
