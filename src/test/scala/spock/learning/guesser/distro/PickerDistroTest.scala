package spock.learning.guesser.distro

import org.scalatest.{FlatSpec, ShouldMatchers}
import spock.Range

class PickerDistroTest extends FlatSpec with ShouldMatchers {

  "A probability distribution" should "have non-negative values" in {
    an [IllegalArgumentException] shouldBe thrownBy {
      PickerDistro(1 -> -0.1, 2 -> 1.1)
    }
  }

  it should "sum one" in {
    an [IllegalArgumentException] shouldBe thrownBy(PickerDistro(1 -> 0.4, 2 -> 0.4))
  }

  it should "reject events out of range" in {
    an [IllegalArgumentException] shouldBe thrownBy(PickerDistro(0 -> 1.0))
    an [IllegalArgumentException] shouldBe thrownBy(PickerDistro(101 -> 1.0))
  }

  it should "be normalized from weighted events" in {
    PickerDistro.normalize(Map(1 -> 0.1, 2 -> 0.4)) shouldBe PickerDistro(1 -> 0.2, 2 -> 0.8)
  }

  it should "created uniformly from a set of values" in {
    PickerDistro.uniform(Set(1, 2, 3, 4)) shouldBe PickerDistro(
      1 -> 0.25,
      2 -> 0.25,
      3 -> 0.25,
      4 -> 0.25
    )
  }

  it should "lookup the probability of an event" in {
    val prob = PickerDistro(1 -> 0.4, 2 -> 0.6)
    prob(1) shouldBe 0.4
    prob(2) shouldBe 0.6
    prob(3) shouldBe 0.0
  }

  it should "lookup the probability of an event range" in {
    val prob = PickerDistro.uniform((1 to 10).toSet)
    prob(Range(1)) shouldBe 0.1
    prob(Range(2, 6)) shouldBe 0.5
    prob(Range.Empty) shouldBe 0
  }

  it should "compute conditional probabilities" in {
    val prob = PickerDistro.uniform((1 to 10).toSet)
    prob.conditional(1, Range(11, 12)) shouldBe 0
    prob.conditional(1, Range(1, 5)) shouldBe 0.2
    prob.conditional(6, Range(1, 5)) shouldBe 0
    prob.conditional(Range(2, 5), Range(1, 5)) shouldBe 0.8
    prob.conditional(Range(1, 8), Range(3, 7)) shouldBe 1
    prob.conditional(Range(1, 10), Range.Empty) shouldBe 0
  }

  it should "compute entropy of ranges" in {
    val prob = PickerDistro.uniform(Set(1, 2))
    prob.entropy(Range.Initial) shouldBe 1
    prob.entropy(Range(1, 2)) shouldBe 1
    prob.entropy(Range(1)) shouldBe 0
    PickerDistro.uniform((1 to 100).toSet).entropy(Range.Initial) should equal (6.643 +- 0.001)
  }
}
