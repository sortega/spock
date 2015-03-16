package spock.simple

import scala.util.Random

import spock._
import spock.Picker.Feedback

/** A linear congruent generator
  *
  * See https://en.wikipedia.org/wiki/Linear_congruential_generator
  */
class CongruentPicker(seed: Int = Random.nextInt()) extends Picker {
  override val pick = seed.abs % MaxValue + 1
  override def next(feedback: Feedback) = new CongruentPicker((pick * 7 + 31) % MaxValue + 1)
  override def toString = "congruent generator"
}
