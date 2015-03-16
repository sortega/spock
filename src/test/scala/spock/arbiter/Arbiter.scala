package spock.arbiter

import scala.annotation.tailrec

import spock.{Picker, Guesser}

class Arbiter(numRounds: Int, debug: Boolean) {

  def apply(picker: Picker, guesser: Guesser): Arbiter.MatchResult = {
    println(s"$picker vs $guesser")
    val rounds = playRounds(picker, guesser)
    Arbiter.MatchResult(rounds.map(_.guesserPoints).sum, rounds)
  }

  private def playRounds(picker: Picker,
                         guesser: Guesser,
                         rounds: Seq[Arbiter.Round] = Seq.empty): Seq[Arbiter.Round] =
    if (rounds.size == numRounds) rounds
    else {
      if (rounds.size % (numRounds / 10) == 1) {
        println(s"  round ${rounds.size}")
      }
      val (nextPicker, nextGuesser, round) = playRound(picker, guesser)
      if (debug) {
        println(round)
      }
      playRounds(nextPicker, nextGuesser, rounds :+ round)
    }

  private def playRound(picker: Picker, guesser: Guesser): (Picker, Guesser, Arbiter.Round) = {
    val startMillis = System.currentTimeMillis()
    val pick = picker.pick

    @tailrec
    def nextAttempt(guesser: Guesser, pastAttempts: Seq[Int] = Seq.empty): (Picker, Guesser, Seq[Int]) = {
      val attempts = pastAttempts :+ guesser.guess
      if (guesser.guess == pick) {
        (picker.next(Picker.Guessed(pastAttempts.size + 1)), guesser.next(Guesser.Guessed), attempts)
      } else if (pastAttempts.size == 4) {
        (picker.next(Picker.NotGuessed), guesser.next(Guesser.NotGuessed), attempts)
      } else {
        val feedback = if (pick > guesser.guess) Guesser.Bigger else Guesser.Smaller
        nextAttempt(guesser.next(feedback), attempts)
      }
    }

    val (nextPicker, nextGuesser, attempts) = nextAttempt(guesser)
    val millis = System.currentTimeMillis() - startMillis
    (nextPicker, nextGuesser, Arbiter.Round(millis, pick, attempts))
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
