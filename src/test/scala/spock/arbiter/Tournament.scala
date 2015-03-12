package spock.arbiter

import spock.arbiter.Arbiter.MatchResult
import spock.{Guesser, Picker}

class Tournament(pickers: Seq[Picker], guessers: Seq[Guesser]) {

  def run(rounds: Int): Unit = {
    val results = runMatches(rounds)
    printMatchesStats(results)
    printPickerRanking(results)
    printGuesserRanking(results)
  }

  private def runMatches(rounds: Int): Map[(Picker, Guesser), MatchResult] = (for {
    picker <- pickers
    guesser <- guessers
    arbiter = new Arbiter(rounds)
  } yield (picker, guesser) -> arbiter(picker, guesser)).toMap

  private def printMatchesStats(results: Map[(Picker, Guesser), MatchResult]): Unit = {
    results.foreach { case ((picker, guesser), result) =>
      val avgTime = result.rounds.map(_.millis.toFloat).sum / result.rounds.size
      println(s"$picker vs $guesser: ${result.pickerPoints} to ${result.guesserPoints} ($avgTime ms/match)")
    }
  }

  private def printPickerRanking(results: Map[(Picker, Guesser), MatchResult]): Unit = {
    val topPickers = for {
      picker <- pickers
      totalScore = results.collect {
        case ((`picker`, _), result) => result.pickerPoints
      }.sum
    } yield picker -> totalScore
    printRanking("picker", topPickers)
  }

  private def printGuesserRanking(results: Map[(Picker, Guesser), MatchResult]): Unit = {
    val topGuessers = for {
      guesser <- guessers
      totalScore = results.collect {
        case ((_, `guesser`), result) => result.guesserPoints
      }.sum
    } yield guesser -> totalScore
    printRanking("guesser", topGuessers)
  }

  private def printRanking(name: String, ranking: Seq[(Any, Int)]): Unit = {
    println(s"$name ranking")
    for ((player, score) <- ranking.sortBy(-_._2)) {
      println("\t% 12d\t%s".format(score, player))
    }
  }
}

