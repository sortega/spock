package spock

import scala.util.Random

import org.scalatest.{FlatSpec, ShouldMatchers}

class FrequencyDistroEstimatorTest extends FlatSpec with ShouldMatchers {

  "A frequency distribution estimator" should "learn with observations" in {
    val estimator = FrequencyDistroEstimator.uniformPrior(1)
    for (_ <- 1 to 100) {
      estimator.learn(Range(1))
    }
    estimator.distro(1) should be > 0.5d
  }

  it should "learn with diffuse observations" in {
    val estimator = FrequencyDistroEstimator.uniformPrior(1)
    for (_ <- 1 to 1000) {
      estimator.learn(Range.NonEmpty(50 - Random.nextInt(30), 50 + Random.nextInt(30)))
    }
    estimator.distro.events.maxBy(_._2)._1 shouldBe 50
  }
}
