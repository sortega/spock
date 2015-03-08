package spock

import java.io._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scalaz.syntax.std.boolean._

import org.scalatest.{FlatSpec, ShouldMatchers}

class LineOrientedIOTest extends FlatSpec with ShouldMatchers {

  class Grep(searchTerm: String) extends LineOrientedIO.Handler {
    override def onLine(line: String): Seq[String] =
      line.contains(searchTerm).option(line).toSeq
  }

  class LineCount extends LineOrientedIO.Handler {
    private var count = 0

    override def onLine(line: String) = {
      count += 1
      Seq.empty
    }

    override def onInputClosed() = Seq(count.toString)
  }

  "Line oriented IO" should "convert input and output lines to streams" in {
    val output = new StringWriter()
    LineOrientedIO(new Grep("hi"), new StringReader("hi\nhello\nhippo\n"), output)
    output.toString shouldBe "hi\nhippo\n"
  }

  it should "be interactive" in {
    val output = new StringWriter()
    val pipeInput = new PipedWriter()
    val pipeOutput = new PipedReader(pipeInput)
    val execution = Future {
      LineOrientedIO(new LineCount, pipeOutput, output)
    }
    pipeInput.write("line1\n")
    pipeInput.write("line2\n")
    Thread.sleep(50)
    execution should not be 'completed
    pipeInput.close()
    Await.ready(execution, 1.second)
    output.toString shouldBe "2\n"
  }
}
