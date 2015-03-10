package spock.arbiter

import spock.Strategy

object BenchmarkMain extends App {

  def printRanking(name: String, ranking: Seq[(Any, Int)]): Unit = {
    println(s"$name ranking")
    for ((player, score) <- ranking.sortBy(-_._2)) {
      println(s"\t$score\t$player")
    }
  }

  val arbiter = new Arbiter(100)
  val results = (for {
    picker <- Strategy.Pickers
    guesser <- Strategy.Guessers
  } yield (picker, guesser) -> arbiter(picker, guesser)).toMap
  results.foreach { case ((picker, guesser), result) =>
    val avgTime = result.rounds.map(_.millis.toFloat).sum / result.rounds.size
    println(s"$picker vs $guesser in $avgTime ms/match: ${result.pickerPoints} to ${result.guesserPoints}")
  }

  val topGuessers = for {
    guesser <- Strategy.Guessers
    totalScore = results.collect {
      case ((_, `guesser`), result) => result.guesserPoints
    }.sum
  } yield guesser -> totalScore
  printRanking("guesser", topGuessers)

  val topPickers = for {
    picker <- Strategy.Pickers
    totalScore = results.collect {
      case ((`picker`, _), result) => result.pickerPoints
    }.sum
  } yield picker -> totalScore
  printRanking("picker", topPickers)
}
