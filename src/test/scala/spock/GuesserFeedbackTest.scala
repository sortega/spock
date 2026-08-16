package spock


import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import spock.Guesser._

class GuesserFeedbackTest extends AnyFlatSpec with Matchers {

  "Guesser feedback" should "be pattern matched" in {
    Feedback.unapply("+") shouldBe Some(Bigger)
    Feedback.unapply("-") shouldBe Some(Smaller)
    Feedback.unapply("=") shouldBe Some(Guessed)
    Feedback.unapply("<>") shouldBe Some(NotGuessed)
    Feedback.unapply("*") shouldBe empty
  }
}
