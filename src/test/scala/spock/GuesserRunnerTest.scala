package spock

import org.scalatest.{FlatSpec, ShouldMatchers}
import spock.Guesser._

class GuesserRunnerTest extends FlatSpec with ShouldMatchers {

  class TestGuesser extends Guesser {
    private var currentGuess = 50

    override def guess = currentGuess

    override def notifyFeedback(feedback: Feedback) = feedback match {
      case Bigger => currentGuess += 1
      case Smaller => currentGuess -= 1
      case Guessed | NotGuessed => currentGuess = 50
    }
  }

  "A guesser runner" should "parse input and format output until input is closed" in {
    val runner = new GuesserRunner(new TestGuesser)
    runner.onStart() shouldBe Seq("50")
    runner.onLine("+") shouldBe Seq("51")
    runner.onLine("=") shouldBe Seq("50")
    runner.onLine("-") shouldBe Seq("49")
    runner.onLine("<>") shouldBe Seq("50")
  }

  it should "stop on unexpected input" in  {
    val runner = new GuesserRunner(new TestGuesser)
    runner.onLine("unexpected") shouldBe Seq("Unexpected input: 'unexpected'")
  }
}
