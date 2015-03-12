package spock.arbiter

import scala.annotation.tailrec

import spock.{Picker, Guesser}

class Arbiter(numRounds: Int, debug: Boolean) {

  def apply(picker: Picker, guesser: Guesser): Arbiter.MatchResult = {
    println(s"$picker vs $guesser")
    val rounds = (1 to numRounds).map { index =>
      if (index % (numRounds / 10) == 1) {
        println(s"  round $index")
      }
      val result = playRound(picker, guesser)
      if (debug) {
        println(result)
      }
      result
    }
    Arbiter.MatchResult(rounds.map(_.guesserPoints).sum, rounds)
  }

  private def playRound(picker: Picker, guesser: Guesser): Arbiter.Round = {
    val startMillis = System.currentTimeMillis()
    val pick = picker.pick

    @tailrec
    def nextAttempt(attempts: Seq[Int] = Seq.empty): Seq[Int] = {
      if (attempts.lastOption == Some(pick)) {
        guesser.notifyFeedback(Guesser.Guessed)
        picker.notifyFeedback(Picker.Guessed(attempts.size))
        attempts
      } else if (attempts.size == 5) {
        guesser.notifyFeedback(Guesser.NotGuessed)
        picker.notifyFeedback(Picker.NotGuessed)
        attempts
      } else {
        attempts.lastOption.foreach { lastAttempt =>
          guesser.notifyFeedback(if (lastAttempt > pick) Guesser.Smaller else Guesser.Bigger)
        }
        nextAttempt(attempts :+ guesser.guess)
      }
    }

    val attempts = nextAttempt()
    val millis = System.currentTimeMillis() - startMillis
    Arbiter.Round(millis, pick, attempts)
  }
}

object Arbiter {
  case class Round(millis: Long, pick: Int, attempts: Seq[Int]) {
    def guesserPoints: Int = if (!attempts.contains(pick)) 0 else 120 - attempts.size * 20
  }
  case class MatchResult(guesserPoints: Int, rounds: Seq[Round]) {
    def pickerPoints: Int = rounds.size * 100 - guesserPoints
  }
}
