package spock.arbiter

import spock.Strategy

object BenchmarkMain extends App {

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
  println(topGuessers.sortBy(-_._2))

  val topPickers = for {
    picker <- Strategy.Pickers
    totalScore = results.collect {
      case ((`picker`, _), result) => result.pickerPoints
    }.sum
  } yield picker -> totalScore
  println(topPickers.sortBy(-_._2))
}
