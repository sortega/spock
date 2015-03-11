package spock.learning.guesser

import spock._

trait ChooseStrategy {
  def choose(attempt: Attempt, range: Range.NonEmpty): Int
}
