package spock.learning

import scalaz.Memo

import spock._

class ExpectedValueStrategy(prob: Distro) {

  type ScoreMap = Map[Int, Double]
  private case class Scope(attempt: Attempt, range: Range)

  def choose(attempt: Attempt, range: Range): Int = {
    val scores = expectedScores(Scope(attempt, range))
    scores.maxBy(_._2)._1
  }

  private val expectedScores: Scope => ScoreMap = Memo.mutableHashMapMemo { scope =>
    val scores = for {
      pivot <- scope.range.iterable
      (leftRange, rightRange) = scope.range.splitBy(pivot)
    } yield {
      def notGuessedScore(remainingRange: Range) = {
        prob.conditional(remainingRange, scope.range) *
          expectedScore(Scope(scope.attempt + 1, remainingRange))
      }

      val whenLower = notGuessedScore(leftRange)
      val whenGuessed = prob.conditional(pivot, scope.range) * Score(scope.attempt)
      val whenGreater = notGuessedScore(rightRange)

      whenLower + whenGuessed + whenGreater
    }
    scope.range.iterable.zip(scores).toMap
  }

  private val expectedScore: Scope => Double = Memo.mutableHashMapMemo { scope =>
    if (scope.range.size == 0) 0
    else if (scope.range.size == 1 || scope.attempt > Attempt.Max) Score(scope.attempt)
    else expectedScores(scope).values.max
  }
}
