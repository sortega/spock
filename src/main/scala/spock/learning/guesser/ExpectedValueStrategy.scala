package spock.learning.guesser

import scalaz.Memo

import spock._

class ExpectedValueStrategy(prob: PickerDistro) {

  import ExpectedValueStrategy._
  type Scores = Array[Double]

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
      def notGuessedScore(remainingRange: Range) = remainingRange match {
        case Range.Empty => 0d
        case nonEmptyRange: Range.NonEmpty =>
          prob.conditional(nonEmptyRange, scope.range) *
            expectedScore(Scope(scope.attempt + 1, nonEmptyRange))
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

private object ExpectedValueStrategy {
  private class Scope private (val attempt: Attempt, val range: Range.NonEmpty)
  private object Scope {
    val scopes = Array.tabulate(Attempt.Max + 1, MaxValue, MaxValue) { (attempt, i, j) =>
      if (i > j) null
      else new Scope(attempt + 1, Range.NonEmpty(i + 1, j + 1))
    }

    def apply(attempt: Attempt, range: Range.NonEmpty): Scope = {
      scopes(attempt - 1)(range.lower - 1)(range.upper - 1)
    }
  }
}
