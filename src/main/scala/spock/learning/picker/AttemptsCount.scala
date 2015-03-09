package spock.learning.picker

import spock.Picker.{Feedback, Guessed, NotGuessed}
import spock._

class AttemptsCount(initialWeight: Double) {
  private val counts: Array[Double] =
    Array(NotGuessed, Guessed(1), Guessed(2), Guessed(3), Guessed(4), Guessed(5))
      .map(PerfectlyRandomHypothesis.scoreProbabilities)
      .map(_ * initialWeight)

  def count(outcome: Picker.Feedback): Unit = {
    counts(indexOf(outcome)) += 1
  }

  def probGiven(outcome: Picker.Feedback): Double = {
    counts(indexOf(outcome)) / counts.sum
  }

  def expectedScore: Double =
    (for ((prob, score) <- counts.zip(AttemptsCount.Scores))
     yield prob * score).sum / counts.sum

  private def indexOf(outcome: Feedback): Attempt = outcome match {
    case NotGuessed => 0
    case Guessed(attempt) => attempt
  }
}

object AttemptsCount {
  val Scores: Vector[Int] = (0 +: (1 to Attempt.Max).toVector.map(Score.apply)).map(100 - _)
}
