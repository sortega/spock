package spock

import scalaz.syntax.std.option._

import org.scalatest.{FlatSpec, ShouldMatchers}
import spock.Guesser._

class GuesserFeedbackTest extends FlatSpec with ShouldMatchers {

  "Guesser feedback" should "be pattern matched" in {
    Feedback.unapply("+") shouldBe Bigger.some
    Feedback.unapply("-") shouldBe Smaller.some
    Feedback.unapply("=") shouldBe Guessed.some
    Feedback.unapply("<>") shouldBe NotGuessed.some
    Feedback.unapply("*") shouldBe 'empty
  }
}
