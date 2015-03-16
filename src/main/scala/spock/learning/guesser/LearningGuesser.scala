package spock.learning.guesser

import spock.Guesser._
import spock.learning.guesser.distro.{DistroEstimator, PickerDistro}
import spock.{Attempt, Guesser, Range}

class LearningGuesser(
   distroEstimator: DistroEstimator,
   strategyFactory: PickerDistro => ChooseStrategy,
   scope: Scope = Scope.Initial) extends Guesser {

  private val strategy = strategyFactory(distroEstimator.distro)
  override val guess = strategy.choose(scope.attempt, scope.range)

  override def next(feedback: Feedback): LearningGuesser = feedback match {
    case Guessed => nextGuesser(distroEstimator.learn(Range(guess)))

    case NotGuessed => nextGuesser(distroEstimator.learn(scope.range))

    case _ if scope.attempt == Attempt.Max =>
      println(s"Impossible input: this is the attempt ${scope.attempt}, " +
        s"it can't be ${feedback.toString.toLowerCase}")
      nextGuesser(distroEstimator)

    case Bigger => narrowRange(scope.range.splitBy(guess)._2, feedback)

    case Smaller => narrowRange(scope.range.splitBy(guess)._1, feedback)
  }

  private def narrowRange(newRange: Range, feedback: Feedback): LearningGuesser = newRange match {
    case Range.Empty =>
      println(s"Impossible input: cannot be ${feedback.toString.toLowerCase} than $guess " +
        s"when narrowed down to ${scope.range} at the attempt ${scope.attempt}")
      nextGuesser()
    case nonEmptyRange: Range.NonEmpty =>
      nextGuesser(nextScope = Scope(scope.attempt + 1, nonEmptyRange))
  }

  private def nextGuesser(nextEstimator: DistroEstimator = distroEstimator, nextScope: Scope = Scope.Initial) =
    new LearningGuesser(nextEstimator, strategyFactory, nextScope)

  override def toString = s"$distroEstimator $strategy guesser"
}
