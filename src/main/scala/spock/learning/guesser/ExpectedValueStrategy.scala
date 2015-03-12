package spock.learning.guesser

import scalaz.Memo

import spock._
import spock.learning.guesser.distro.PickerDistro
import spock.util.Choose

class ExpectedValueStrategy(prob: PickerDistro) extends ChooseStrategy {

  type Scores = Array[Double]

  override def choose(attempt: Attempt, range: Range.NonEmpty): Int = {
    val scores = expectedScores(Scope(attempt, range))
    val bestScore = scores.max
    val eligible = scores.zipWithIndex.collect {
      case (score, index) if score + Eps >= bestScore => range.lower + index
    }
    Choose.randomly(eligible)
  }

  private val expectedScoresCache = new collection.mutable.HashMap[Scope, Scores] {
    override protected def initialSize = 16384
  }
  private val expectedScoreCache = new collection.mutable.HashMap[Scope, Double] {
    override protected def initialSize = 32768
  }

  private val expectedScores: Scope => Scores = Memo.mutableMapMemo(expectedScoresCache) { scope =>
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

  private val expectedScore: Scope => Double = Memo.mutableMapMemo(expectedScoreCache) { scope =>
    if (scope.range.size == 0) 0
    else if (scope.range.size == 1 || scope.attempt > Attempt.Max) Score(scope.attempt)
    else expectedScores(scope).max
  }

  override def toString = "expected-value"
}
