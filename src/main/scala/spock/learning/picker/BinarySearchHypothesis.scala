package spock.learning.picker

import spock.Picker._
import spock.learning.picker.GuesserHypothesis.Observation

case object BinarySearchHypothesis extends GuesserHypothesis {

  private val outcomes = Map(
    1 -> NotGuessed, 2 -> NotGuessed, 3 -> Guessed(5), 4 -> NotGuessed, 5 -> NotGuessed,
    6 -> Guessed(4), 7 -> NotGuessed, 8 -> NotGuessed, 9 -> Guessed(5), 10 -> NotGuessed,
    11 -> NotGuessed, 12 -> Guessed(3), 13 -> NotGuessed, 14 -> NotGuessed, 15 -> Guessed(5),
    16 -> NotGuessed, 17 -> NotGuessed, 18 -> Guessed(4), 19 -> NotGuessed, 20 -> NotGuessed,
    21 -> Guessed(5), 22 -> NotGuessed, 23 -> NotGuessed, 24 -> NotGuessed, 25 -> Guessed(2),
    26 -> NotGuessed, 27 -> NotGuessed, 28 -> Guessed(5), 29 -> NotGuessed, 30 -> NotGuessed,
    31 -> Guessed(4), 32 -> NotGuessed, 33 -> NotGuessed, 34 -> Guessed(5), 35 -> NotGuessed,
    36 -> NotGuessed, 37 -> Guessed(3), 38 -> NotGuessed, 39 -> NotGuessed, 40 -> Guessed(5),
    41 -> NotGuessed, 42 -> NotGuessed, 43 -> Guessed(4), 44 -> NotGuessed, 45 -> NotGuessed,
    46 -> Guessed(5), 47 -> NotGuessed, 48 -> NotGuessed, 49 -> NotGuessed, 50 -> Guessed(1),
    51 -> NotGuessed, 52 -> NotGuessed, 53 -> Guessed(5), 54 -> NotGuessed, 55 -> NotGuessed,
    56 -> Guessed(4), 57 -> NotGuessed, 58 -> NotGuessed, 59 -> Guessed(5), 60 -> NotGuessed,
    61 -> NotGuessed, 62 -> Guessed(3), 63 -> NotGuessed, 64 -> NotGuessed, 65 -> Guessed(5),
    66 -> NotGuessed, 67 -> NotGuessed, 68 -> Guessed(4), 69 -> NotGuessed, 70 -> NotGuessed,
    71 -> Guessed(5), 72 -> NotGuessed, 73 -> NotGuessed, 74 -> NotGuessed, 75 -> Guessed(2),
    76 -> NotGuessed, 77 -> NotGuessed, 78 -> Guessed(5), 79 -> NotGuessed, 80 -> NotGuessed,
    81 -> Guessed(4), 82 -> NotGuessed, 83 -> NotGuessed, 84 -> Guessed(5), 85 -> NotGuessed,
    86 -> NotGuessed, 87 -> NotGuessed, 88 -> Guessed(3), 89 -> NotGuessed, 90 -> NotGuessed,
    91 -> Guessed(5), 92 -> NotGuessed, 93 -> NotGuessed, 94 -> Guessed(4), 95 -> NotGuessed,
    96 -> NotGuessed, 97 -> Guessed(5), 98 -> NotGuessed, 99 -> NotGuessed, 100 -> NotGuessed
  )

  override val expectedScores =
    ExpectedScores(outcomes.toVector.sortBy(_._1).map(_._2.pickerScore.toDouble))

  private val possibleObservations = outcomes.map((Observation.apply _).tupled).toSet

  override def probGiven(observation: Observation): Double =
    if (possibleObservations.contains(observation)) 1 else 0
}
