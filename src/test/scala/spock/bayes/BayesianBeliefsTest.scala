package spock.bayes

import org.scalatest.{FlatSpec, ShouldMatchers}
import spock.Eps

class BayesianBeliefsTest extends FlatSpec with ShouldMatchers {

  case class Machine(defectRate: Double) extends BayesianBeliefs.Hypothesis[Boolean] {
    override def probGiven(failure: Boolean): Double = {
      if (failure) defectRate else 1 - defectRate
    }
  }

  val A = Machine(0.05)
  val B = Machine(0.03)
  val C = Machine(0.01)

  // https://en.wikipedia.org/wiki/Bayes%27_theorem#Introductory_example
  "The machines example" should "be successfully computed" in {
    val PriorBeliefs = BayesianBeliefs[Boolean, Machine](Map(A -> 0.2, B -> 0.3, C -> 0.5))
    PriorBeliefs.update(true).hypotheses(C) should equal (5.0/24.0 +- Eps)
  }

  case class Dice(faces: Int) extends BayesianBeliefs.Hypothesis[Int] {
    override def probGiven(roll: Int): Double =
      if (roll >= 1 && roll <= faces) 1.0 / faces else 0
  }

  val D6 = Dice(6)
  val D10 = Dice(10)
  val D12 = Dice(12)

  "The dices domain" should "drop belief to 0 when an impossible roll is seen" in {
    val PriorBeliefs = BayesianBeliefs[Int, Dice](Map(D6 -> 0.5, D10 -> 0.35, D12 -> 0.35))
    val after1 = PriorBeliefs.update(1)
    after1.hypotheses.values.forall(_ > 0) shouldBe true
    val after7 = after1.update(7)
    after7.hypotheses(D6) shouldBe 0
    val after11 = after7.update(11)
    after11.hypotheses shouldBe Map(D6 -> 0d, D10 -> 0d, D12 -> 1d)
    an [IllegalArgumentException] shouldBe thrownBy {
      after7.update(100)
    }
  }
}
