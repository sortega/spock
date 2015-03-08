package spock

import scalaz.syntax.std.option._

import org.scalatest.{FlatSpec, ShouldMatchers}
import spock.Picker.{Guessed, NotGuessed}

class PickerFeedbackTest extends FlatSpec with ShouldMatchers {

  "Picker feedback" should "be parsed from string" in {
    Picker.Feedback.parse("not guessed") shouldBe NotGuessed.some
    Picker.Feedback.parse("guessed at 1") shouldBe Guessed(1).some
    Picker.Feedback.parse("guessed at 5") shouldBe Guessed(5).some
    Picker.Feedback.parse("guessed at -1") shouldBe None
    Picker.Feedback.parse("not a result") shouldBe None
  }
}
