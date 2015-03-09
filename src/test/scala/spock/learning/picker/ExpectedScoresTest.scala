package spock.learning.picker

import org.scalatest.{FlatSpec, ShouldMatchers}

class ExpectedScoresTest extends FlatSpec with ShouldMatchers {

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
