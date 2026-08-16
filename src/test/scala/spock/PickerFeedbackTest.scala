package spock


import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import spock.Picker.{Guessed, NotGuessed}

class PickerFeedbackTest extends AnyFlatSpec with Matchers {

  "Picker feedback" should "be parsed from string" in {
    Picker.Feedback.parse("not guessed") shouldBe Some(NotGuessed)
    Picker.Feedback.parse("guessed at 1") shouldBe Some(Guessed(1))
    Picker.Feedback.parse("guessed at 5") shouldBe Some(Guessed(5))
    Picker.Feedback.parse("guessed at -1") shouldBe None
    Picker.Feedback.parse("not a result") shouldBe None
  }
}
