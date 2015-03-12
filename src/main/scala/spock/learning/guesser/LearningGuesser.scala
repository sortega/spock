package spock.learning.guesser

import spock.Guesser._
import spock.learning.guesser.distro.{DistroEstimator, PickerDistro}
import spock.{Attempt, Guesser, Range}

class LearningGuesser(
   distroEstimator: DistroEstimator,
   strategyFactory: PickerDistro => ChooseStrategy) extends Guesser {

  private var strategy: ChooseStrategy = _
  private var scope: Scope = _
  private var currentGuess: Int = _

  nextRound()

  override def guess = currentGuess

  override def notifyFeedback(feedback: Feedback): Unit = feedback match {
    case Guessed =>
      distroEstimator.learn(Range(guess))
      nextRound()

    case NotGuessed =>
      distroEstimator.learn(scope.range)
      nextRound()

    case _ if scope.attempt == Attempt.Max =>
      println(s"Impossible input: this is the attempt ${scope.attempt}, " +
        s"it can't be ${feedback.toString.toLowerCase}")
      nextRound()

    case Bigger =>
      narrowRange(scope.range.splitBy(guess)._2, feedback)

    case Smaller =>
      narrowRange(scope.range.splitBy(guess)._1, feedback)
  }

  private def narrowRange(newRange: Range, feedback: Feedback): Unit = newRange match {
    case Range.Empty =>
      println(s"Impossible input: cannot be ${feedback.toString.toLowerCase} than $guess " +
        s"when narrowed down to ${scope.range} at the attempt ${scope.attempt}")
      nextRound()
    case nonEmptyRange: Range.NonEmpty =>
      updateScope(Scope(scope.attempt + 1, nonEmptyRange))
  }

  private def nextRound() = {
    strategy = strategyFactory(distroEstimator.distro)
    updateScope(Scope.Initial)
  }

  private def updateScope(newScope: Scope): Unit = {
    scope = newScope
    currentGuess = strategy.choose(scope.attempt, scope.range)
  }

  override def toString = s"$distroEstimator $strategy guesser"
}
