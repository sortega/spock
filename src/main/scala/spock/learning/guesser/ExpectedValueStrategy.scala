package spock.learning.guesser

import scalaz.Memo

import spock._

class ExpectedValueStrategy(prob: PickerDistro) {

  type Scores = Array[Double]
  private case class Scope(attempt: Attempt, range: Range)

  def choose(attempt: Attempt, range: Range.NonEmpty): Int = {
    val scores = expectedScores(Scope(attempt, range))
    range.lower + scores.indexOf(scores.max)
  }

  private val expectedScores: Scope => Scores = Memo.mutableHashMapMemo { scope =>
    val scores = Array.fill(scope.range.size)(0d)
    for {
      (pivot, index) <- scope.range.iterable.zipWithIndex
      (leftRange, rightRange) = scope.range.splitBy(pivot)
    } yield {
      def notGuessedScore(remainingRange: Range) = {
        prob.conditional(remainingRange, scope.range) *
          expectedScore(Scope(scope.attempt + 1, remainingRange))
      }

      val whenLower = notGuessedScore(leftRange)
      val whenGuessed = prob.conditional(pivot, scope.range) * Score(scope.attempt)
      val whenGreater = notGuessedScore(rightRange)

      scores(index) += whenLower + whenGuessed + whenGreater
    }
    scores
  }

  private val expectedScore: Scope => Double = Memo.mutableHashMapMemo { scope =>
    if (scope.range.size == 0) 0
    else if (scope.range.size == 1 || scope.attempt > Attempt.Max) Score(scope.attempt)
    else expectedScores(scope).max
  }
}
