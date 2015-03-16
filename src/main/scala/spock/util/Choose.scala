package spock.util

import java.security.SecureRandom

object Choose {
  val Random = new scala.util.Random(new SecureRandom())

  def best[A](weightedElements: Map[A, Double]): A = best(weightedElements.toSeq)

  def best[A](weightedElements: Seq[(A, Double)]): A = {
    weightedElements.maxBy(_._2)._1
  }

  def randomly[A](elems: Seq[A]): A = elems(Random.nextInt(elems.size))

  def randomlyOverPercentile[A](percentile: Double, weightedElements: Map[A, Double]): A =
    randomlyOverPercentile(percentile, weightedElements.toSeq)

  def randomlyOverPercentile[A](percentile: Double, weightedElements: Seq[(A, Double)]): A = {
    val threshold = percentile * weightedElements.maxBy(_._2)._2
    val eligible = weightedElements.collect {
      case (element, weight) if weight >= threshold => element
    }
    Choose.randomly(eligible)
  }
}
