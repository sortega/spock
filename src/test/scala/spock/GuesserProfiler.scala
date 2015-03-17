package spock

import scala.annotation.tailrec

import spock.Guesser.{Bigger, Smaller}
import spock.Picker.{Guessed, NotGuessed}
import spock.learning.picker.{ProfiledGuesserHypothesis, AttemptsCount}
import spock.simple.{RandomBinaryGuesser, Random31Guesser}

class GuesserProfiler(build: => Guesser) {

  def gatherProfile(samplesPerValue: Int): ProfiledGuesserHypothesis = {
    new AttemptsCount(initialWeight = 0)
    val profile = (
      for (value <- MinValue to MaxValue)
      yield value -> feedbackDistroFor(samplesPerValue, value)).toMap
    ProfiledGuesserHypothesis(profile)
  }

  private def feedbackDistroFor(samplesPerValue: Int, value: Int): ProfiledGuesserHypothesis.FeedbackDistro = {
    val counts = Seq.fill(samplesPerValue)(feedbackFor(value, build))
      .groupBy(identity)
      .mapValues(_.size)
    ProfiledGuesserHypothesis.FeedbackDistro(counts.mapValues(_.toDouble / samplesPerValue))
  }

  @tailrec
  private def feedbackFor(value: Int, guesser: Guesser, attempt: Int = 1): Picker.Feedback =
    if (guesser.guess == value) Guessed(attempt)
    else if (attempt == 5) NotGuessed
    else feedbackFor(
      value, guesser.next(if (value < guesser.guess) Smaller else Bigger), attempt + 1)
}

object GuesserProfiler extends App {
  println(new GuesserProfiler(RandomBinaryGuesser()).gatherProfile(10000))
}
