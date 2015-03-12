package spock.learning.guesser

import spock._

class Scope private (val attempt: Attempt, val range: Range.NonEmpty)

object Scope {
  private val scopes = Array.tabulate(Attempt.Max + 1, MaxValue, MaxValue) { (attempt, i, j) =>
    if (i > j) null
    else new Scope(attempt + 1, Range.NonEmpty(i + 1, j + 1))
  }

  val Initial = Scope(1, Range.Initial)

  def apply(attempt: Attempt, range: Range.NonEmpty): Scope = {
    scopes(attempt - 1)(range.lower - 1)(range.upper - 1)
  }
}
