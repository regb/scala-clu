package example

import clu._

object Main {

  object FlagCounter extends IntFlagDef("counter", "A counter flag", Some(1), Some('c'))

  def main(args: Array[String]): Unit = {

    val command = Command(
      "example", "Run an example command",
"""The example command is just that, an example command.

Example:
  example --counter 4
""", List(), List(FlagCounter), () => execute())

    println("args: " + args.toList)

    command.main(new ArraySlice(args, 0, args.length))
  }

  def execute(): Int = {
    println("Hello World")
    0
  }

}
