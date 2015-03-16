package spock.learning.guesser

import scala.util.Random

import org.scalatest.{FlatSpec, ShouldMatchers}
import spock.Range

class FrequencyPickerDistroEstimatorTest extends FlatSpec with ShouldMatchers {

  "A frequency distribution estimator" should "learn with observations" in {
    val estimator = FrequencyDistroEstimator.uniformPrior(1)
      .learn(Seq.fill(100)(Range(1)))
    estimator.distro(1) should be > 0.5d
  }

  it should "learn with diffuse observations" in {
    val estimator = FrequencyDistroEstimator.uniformPrior(1)
      .learn(Seq.fill(1000)(Range.NonEmpty(50 - Random.nextInt(30), 50 + Random.nextInt(30))))
    estimator.distro.events.maxBy(_._2)._1 shouldBe 50
  }
}
