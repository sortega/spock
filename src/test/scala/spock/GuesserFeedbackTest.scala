package spock

import scalaz.syntax.std.option._

import org.scalatest.{FlatSpec, ShouldMatchers}
import spock.Guesser.Bigger

class GuesserFeedbackTest extends FlatSpec with ShouldMatchers {

  "Guesser feedback" should "be pattern matched" in {
    Bigger.unapply("+") shouldBe Bigger.some
    Bigger.unapply("-") shouldBe 'empty
  }
}
