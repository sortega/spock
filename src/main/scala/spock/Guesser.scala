package spock

trait Guesser {
  val guess: Int
  def next(feedback: Guesser.Feedback): Guesser
}

object Guesser {
  sealed trait Feedback {
    def symbol: String
  }
  object Feedback {
    def unapply(str: String): Option[Feedback] =
      Seq(Bigger, Smaller, Guessed, NotGuessed).find(_.symbol == str)
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
