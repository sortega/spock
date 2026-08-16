# The 1-to-100 Guessing Game — Player Specification

Your task is to implement a program that plays both roles of an iterated
number-guessing game against other programs, coordinated by a referee (the
"coordinator"). This document is the complete contract: game rules, command-line
interface, wire protocol, error handling, and robustness requirements.

## 1. The game

Two players: a **picker** and a **guesser**.

1. The picker secretly chooses an integer between **1 and 100** (inclusive).
2. The guesser has **up to 5 attempts** to find it. After each wrong attempt the
   guesser is told whether the picked number is **greater** or **lower** than the
   guess.
3. Scoring for the round:

   | Guessed on attempt | Guesser points | Picker points |
   |--------------------|----------------|---------------|
   | 1                  | 100            | 0             |
   | 2                  | 80             | 20            |
   | 3                  | 60             | 40            |
   | 4                  | 40             | 60            |
   | 5                  | 20             | 80            |
   | not guessed        | 0              | 100           |

A match consists of **10,000 rounds** with your program in one role, and another
10,000 rounds with roles swapped (played as a separate process run). The same two
programs face each other for the whole match, so both sides can (and should)
adapt: a picker that always picks 50, or picks uniformly at random, will be
exploited by a learning guesser — and vice versa. Note that plain binary search
guarantees at most attempt 5 wins against an adversarial picker; good strategies
model their opponent.

## 2. Command-line interface

Your submission is a single executable (any language) invoked as:

```sh
<program> guess   # play the guesser role
<program> pick    # play the picker role
```

One process plays one role for an entire 10,000-round match. The process
communicates with the coordinator through **stdin/stdout** and must keep running
until stdin is closed, then exit.

## 3. Wire protocol

- Line-oriented: every message is a single line terminated by `\n` (UTF-8, no
  carriage returns).
- **Flush stdout after every line you write.** Buffered, unflushed output
  deadlocks the match and counts as a crash.
- **stdout is exclusively for protocol messages.** Send logs and diagnostics to
  stderr (which is ignored by the coordinator).
- **Emit nothing until the coordinator commands you.** The coordinator always
  speaks first.

### 3.1 Guesser role (`<program> guess`)

The coordinator starts each round by sending the command line `guess`. You reply
with your first guess: a plain integer `1`..`100`. After each guess the
coordinator sends exactly one feedback line:

| Coordinator sends | Meaning                                | Your reply                     |
|-------------------|----------------------------------------|--------------------------------|
| `greater`         | picked number is greater than the guess| next guess, immediately        |
| `lower`           | picked number is lower than the guess  | next guess, immediately        |
| `guessed`         | correct — round over                   | **nothing**; wait for `guess`  |
| `not-guessed`     | 5th attempt wrong — round over         | **nothing**; wait for `guess`  |

You will never receive `greater`/`lower` after your 5th attempt; the round always
ends in `guessed` or `not-guessed`.

Example session (coordinator lines marked `>`, your lines `<`):

```
> guess
< 50
> greater
< 75
> lower
< 62
> guessed
> guess
< 47
> lower
< 23
...
```

### 3.2 Picker role (`<program> pick`)

The coordinator starts each round with the command line `pick`. You reply with
your picked number: a plain integer `1`..`100`. You must commit to the number
before knowing any guesses — the coordinator relays it to the guesser's feedback
honestly on your behalf. When the round ends the coordinator sends exactly one
result line:

| Coordinator sends | Meaning                                    | Your reply                    |
|-------------------|--------------------------------------------|-------------------------------|
| `guessed N`       | guesser found it on attempt N (1..5)       | **nothing**; wait for `pick`  |
| `not-guessed`     | guesser failed all 5 attempts              | **nothing**; wait for `pick`  |

Example session:

```
> pick
< 87
> guessed 4
> pick
< 13
> not-guessed
> pick
< 42
...
```

### 3.3 Errors and resynchronization

At any point where a reply is expected, you may instead send a line:

```
error: <arbitrary text>
```

Do this whenever you receive input you cannot parse or that is invalid in the
current state. After sending an error line, discard the current round and return
to idle: wait for the next `guess`/`pick` command. The commands that start rounds
are the resynchronization points of the protocol.

Coordinator behavior on an error (whether an `error:` line or any reply it cannot
parse):

- The round is aborted and scored as the **worst outcome for the erring side**:
  an erring guesser scores 0 (as not-guessed); an erring picker concedes 100
  points to the guesser.
- If the other player's round is still open, the coordinator closes it with
  `not-guessed` before starting the next round.
- The match continues with the next round's `guess`/`pick` command.

Errors are never fatal to the match — but every errored round is points lost, so
treat them as a safety net, not a strategy.

## 4. Robustness requirements

Your program must:

- Survive a full 10,000-round match per role: no crashes, no unbounded memory
  growth, no per-round slowdown that accumulates (keep the average well under
  ~10 ms per reply).
- Never deadlock: always either reply (number or `error:` line) or wait for
  input, per the state machines above; always flush after writing.
- Tolerate arbitrary garbage on stdin (respond with `error: ...` and resync)
  without crashing.
- Exit cleanly (status 0) when stdin reaches end-of-file.
- Not read or write files, spawn processes, or use the network — stdin/stdout
  (plus stderr for logs) is the whole world.

## 5. What gets measured

Conformance to this protocol comes first: a program that crashes, deadlocks, or
floods stdout with non-protocol output forfeits. Among conforming programs, the
score is the total points accumulated over both 10,000-round halves against each
opponent. Play well in both roles: a brilliant guesser with a naive picker loses
on aggregate.
