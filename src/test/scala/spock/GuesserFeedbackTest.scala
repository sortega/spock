package spock

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import spock.Guesser._

class GuesserFeedbackTest extends AnyFlatSpec with Matchers {

  "Guesser feedback" should "be pattern matched" in {
    Feedback.unapply("greater") shouldBe Some(Greater)
    Feedback.unapply("lower") shouldBe Some(Lower)
    Feedback.unapply("guessed") shouldBe Some(Guessed)
    Feedback.unapply("not-guessed") shouldBe Some(NotGuessed)
    Feedback.unapply("*") shouldBe empty
  }
}
