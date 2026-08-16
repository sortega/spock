package spock

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import spock.Guesser._

class GuesserRunnerTest extends AnyFlatSpec with Matchers {

  case class TestGuesser(override val guess: Int = 50) extends Guesser {
    override def next(feedback: Feedback) = feedback match {
      case Greater => copy(guess + 1)
      case Lower => copy(guess - 1)
      case Guessed | NotGuessed => TestGuesser()
    }
  }

  "A guesser runner" should "emit nothing until commanded" in {
    val runner = new GuesserRunner(new TestGuesser)
    runner.onStart() shouldBe empty
  }

  it should "play rounds started by the guess command" in {
    val runner = new GuesserRunner(new TestGuesser)
    runner.onLine("guess") shouldBe Seq("50")
    runner.onLine("greater") shouldBe Seq("51")
    runner.onLine("lower") shouldBe Seq("50")
    runner.onLine("guessed") shouldBe empty
    runner.onLine("guess") shouldBe Seq("50")
    runner.onLine("not-guessed") shouldBe empty
  }

  it should "report an error on unexpected input while idle and keep waiting" in {
    val runner = new GuesserRunner(new TestGuesser)
    runner.onLine("greater") shouldBe Seq("error: unexpected input 'greater'")
    runner.onLine("guess") shouldBe Seq("50")
  }

  it should "abandon the round on unexpected feedback and resync on the next command" in {
    val runner = new GuesserRunner(new TestGuesser)
    runner.onLine("guess") shouldBe Seq("50")
    runner.onLine("greater") shouldBe Seq("51")
    runner.onLine("bogus") shouldBe Seq("error: unexpected input 'bogus'")
    runner.onLine("guess") shouldBe Seq("50")
  }
}
