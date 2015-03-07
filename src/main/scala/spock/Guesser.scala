package spock

import scalaz.syntax.std.boolean._

trait Guesser {
  def guess: Int
  def notifyFeedback(feedback: Guesser.Feedback): Unit
}

object Guesser {
  sealed trait Feedback {
    def symbol: String
    def unapply(str: String): Option[this.type] = (str == this.symbol).option(this)
  }
  case object Bigger extends Feedback {
    override val symbol = "+"
  }
  case object Smaller extends Feedback {
    override val symbol = "-"
  }
  case object Guessed extends Feedback {
    override val symbol = "="
  }
  case object NotGuessed extends Feedback {
    override val symbol = "<>"
  }
}
