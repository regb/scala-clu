package example

import clu._

object Main {

  object FlagCounter extends IntFlagDef("counter", "A counter flag", Some(1), Some('c'))

  object Arg1 extends IntArgDef("arg1", "An integer argument")

  def main(args: Array[String]): Unit = {

    val command1 = Command(
      "cmd1", "Run an example command",
"""The example command is just that, an example command.

Example:
  example --counter 4
""", List(Arg1), List(FlagCounter), () => execute())

    val command = MultiCommands("multi-example", List(command1))

    command.main(new ArraySlice(args, 0, args.length))
  }

  def execute(): Int = {
    println("Hello World")
    println(s"flag counter=${FlagCounter.value}")
    println(s"arg1=${Arg1.value}")
    0
  }

}
