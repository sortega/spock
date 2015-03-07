package object spock {

  type Attempt = Int
  object Attempt {
    val Max = 5
  }

  val MinValue = 1
  val MaxValue = 100

  val Score = Map[Attempt, Int](
    1 -> 100,
    2 ->  80,
    3 ->  60,
    4 ->  40,
    5 ->  20
  ).withDefaultValue(0)

  val Eps = 0.0000001
}
