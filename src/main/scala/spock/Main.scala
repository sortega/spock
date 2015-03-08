package spock

import spock.forgetful.{NaiveForgetfulGuesser, NaiveForgetfulPicker}

object Main {
  def main(args: Array[String]): Unit = args match {
    case Array("pick") =>
      LineOrientedIO(new PickerRunner(new NaiveForgetfulPicker))
    case Array("guess") =>
      LineOrientedIO(new GuesserRunner(new NaiveForgetfulGuesser))
    case unexpected =>
      println("Unexpected arguments: " + unexpected)
      println("usage: java -jar <this jar> (pick | guess)")
  }
}
