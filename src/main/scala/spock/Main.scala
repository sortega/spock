package spock

object Main {
  def main(args: Array[String]): Unit = args match {
    case Array("pick") =>
      println("Not yet implemented")
    case Array("guess") =>
      LineOrientedIO(new GuesserRunner(new NaiveForgetfulGuesser))
    case unexpected =>
      println("Unexpected arguments: " + unexpected)
      println("usage: java -jar <this jar> (pick | guess)")
  }
}
