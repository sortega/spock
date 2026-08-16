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
      Seq(Greater, Lower, Guessed, NotGuessed).find(_.symbol == str)
  }
  case object Greater extends Feedback {
    override val symbol = "greater"
  }
  case object Lower extends Feedback {
    override val symbol = "lower"
  }
  case object Guessed extends Feedback {
    override val symbol = "guessed"
  }
  case object NotGuessed extends Feedback {
    override val symbol = "not-guessed"
  }
}
