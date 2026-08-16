Spock
=====

An exploration of the possible strategies for the iterated game described of
guessing numbers described in the
[Art of the Strategy](http://www.artofstrategy.net/1to100.html).

It is named Spock because some of the implemented strategies use bayesian
reasoning and in honor of the recently deceased Leonard Nimoy.

You can try the different strategies by plugging the values defined in the
`Strategy` object in the main function.  Enjoy!

Requirements
------------

- sbt (any recent launcher; the project pins its own sbt version)
- A modern JDK. On this machine the default JDK is ancient, so prefix sbt
  commands with:

  ```sh
  export JAVA_HOME=/opt/homebrew/opt/openjdk/libexec/openjdk.jdk/Contents/Home
  ```

Running the tests
-----------------

```sh
sbt test                                # full suite
sbt "testOnly spock.RangeTest"          # a single suite
sbt "Test/runMain spock.arbiter.BenchmarkMain"  # in-process 10k-round tournament
```

Getting an executable
---------------------

```sh
sbt assembly
```

This produces `./spock` in the project directory: a fat jar with a launcher
script prepended, so it is directly executable (it still needs `java` on the
PATH):

```sh
./spock guess   # play as guesser
./spock pick    # play as picker
```

Both roles speak the line-oriented protocol on stdin/stdout defined in
`spec.md`. Rounds are started by the coordinator (`guess` / `pick` commands);
the player answers with numbers, receives `greater`/`lower`/`guessed`/`not-guessed`
(guesser) or `guessed N`/`not-guessed` (picker) and reports unparseable input as
`error: <text>` before waiting for the next round command. For example (numbers
will vary):

```sh
$ printf 'guess\ngreater\nlower\nguessed\n' | ./spock guess
35
61
47
$ printf 'pick\nguessed 3\npick\nnot-guessed\n' | ./spock pick
41
64
```

Note: recent JDKs
print a `sun.misc.Unsafe` deprecation warning on stderr at startup; stdout is
unaffected.

A fully self-contained native binary (no JVM required) would need GraalVM
`native-image`; not set up for now since the executable jar is enough for
tournament use.
