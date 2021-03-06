package clu

case class MultiCommands(
  name: String,
  commands: Seq[Command]
) {

  def main(args: ArraySlice[String]): Unit = {
    if(!args.hasNext) {
      sys.error(s"Missing command selection")
    }
    commands.find(_.name == args.head) match {
      case Some(cmd) => {
        args.next()
        cmd.main(args)
      }
      case None => {
        sys.error(s"Unknown command ${args.head}")
      }
    }
  }

  def help: String = {
    "TODO"
  }

}

case class Command(
  name: String,
  usage: String,
  description: String,
  args: List[ArgDef[_]],
  flags: List[FlagDef[_]],
  execute: () => Int
) {
  def main(args: ArraySlice[String]): Unit = {
    FlagDef.processFlags(this.flags)(args)
    ArgDef.processArgs(this.args)(args)
    if(args.nonEmpty) {
      sys.error("Unexpected argument: " + args.head)
    }
    sys.exit(execute())
  }

  // Auto-generated help message based on the Command description.
  def help: String = {
    "TODO"
  }
}

class ArraySlice[A](array: Array[A], private var from: Int, private var length: Int) {
  require(from >= 0 && from + length <= array.length)

  def apply(i: Int): A = array(from + i)

  def head: A = array(from)

  def isEmpty: Boolean = length == 0
  def nonEmpty = !isEmpty

  def hasNext: Boolean = length > 0
  def next(): A = {
    from += 1
    length -= 1
    array(from-1)
  }

}
