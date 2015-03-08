package spock

import java.io._

/** Adapts a pure function into a line-oriented IO process */
object LineOrientedIO {

  trait Handler {
    def onStart(): Seq[String] = Seq.empty
    def onLine(line: String): Seq[String]
    def onInputClosed(): Seq[String] = Seq.empty
  }

  def apply(handler: Handler): Unit = {
    apply(handler, new InputStreamReader(System.in), new OutputStreamWriter(System.out))
  }

  def apply(handler: Handler, input: Reader, output: Writer): Unit = {

    def writeLine(line: String) = {
      output.write(line + "\n")
      output.flush()
    }

    handler.onStart().foreach(writeLine)

    val reader = new BufferedReader(input)
    Stream.continually(reader.readLine())
      .takeWhile(_ != null)
      .foreach(line => handler.onLine(line).foreach(writeLine))

    handler.onInputClosed().foreach(writeLine)
  }
}
