package spock

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import spock.Picker.Feedback

class PickerRunnerTest extends AnyFlatSpec with Matchers {

  case class TestPicker(override val pick: Int = 42, results: Seq[Feedback] = Seq.empty)
      extends Picker {
    override def next(feedback: Feedback) = copy(pick + 1, results :+ feedback)
  }

  "A picker runner" should "emit nothing until commanded" in {
    val runner = new PickerRunner(new TestPicker)
    runner.onStart() shouldBe empty
  }

  it should "play rounds started by the pick command" in {
    val runner = new PickerRunner(new TestPicker)
    runner.onLine("pick") shouldBe Seq("42")
    runner.onLine("guessed 3") shouldBe empty
    runner.onLine("pick") shouldBe Seq("43")
    runner.onLine("not-guessed") shouldBe empty
    runner.onLine("pick") shouldBe Seq("44")
  }

  it should "report an error on unexpected input while idle and keep waiting" in {
    val runner = new PickerRunner(new TestPicker)
    runner.onLine("guessed 3") shouldBe Seq("error: unexpected input 'guessed 3'")
    runner.onLine("pick") shouldBe Seq("42")
  }

  it should "abandon the round on an unexpected result and resync on the next command" in {
    val runner = new PickerRunner(new TestPicker)
    runner.onLine("pick") shouldBe Seq("42")
    runner.onLine("bogus") shouldBe Seq("error: unexpected input 'bogus'")
    runner.onLine("pick") shouldBe Seq("43")
  }
}
