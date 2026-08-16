package spock


trait Picker {
  val pick: Int
  def next(feedback: Picker.Feedback): Picker
}

object Picker {
  sealed trait Feedback {
    def guesserScore: Int
    def pickerScore = Score(1) - guesserScore
  }
  case object NotGuessed extends Feedback {
    override def guesserScore = 0
  }
  case class Guessed(attempt: Int) extends Feedback {
    override def guesserScore = Score(attempt)
  }

  object Feedback {
    private val GuessedPattern = "guessed at ([1-5])".r

    def parse(str: String): Option[Feedback] = str match {
      case "not guessed" => Some(NotGuessed)
      case GuessedPattern(attempt) => Some(Guessed(attempt.toInt))
      case _ => None
    }
  }
}
