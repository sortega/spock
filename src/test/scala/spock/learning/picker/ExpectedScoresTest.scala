package spock.learning.picker

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ExpectedScoresTest extends AnyFlatSpec with Matchers {

  "Guesser expected scores" should "have 100 elements" in {
    an [IllegalArgumentException] shouldBe thrownBy {
      ExpectedScores(Vector.fill(99)(1))
    }
  }

  it should "have non-negative expected scores" in {
    an [IllegalArgumentException] shouldBe thrownBy {
      ExpectedScores(Vector.fill(99)(0d) :+ -1d)
    }
  }
}
