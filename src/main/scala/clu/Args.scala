package clu

abstract class ArgDef[A] {

  val name: String
  val description: String

  private var value_ : Option[A] = None

  def value: A = value_.get

  def parseAndSetValue(v: String): Option[A] = {
    value_ = parseValue(v)
    value_
  }

  def parseValue(v: String): Option[A]
}

object ArgDef {

  def processArgs(argDefs: List[ArgDef[_]])(args: ArraySlice[String]): Unit = {
    var expectedArgs: List[ArgDef[_]] = argDefs
    while(args.hasNext && expectedArgs.nonEmpty) {
      val nextArg = args.next()
      if(expectedArgs.head.parseAndSetValue(nextArg).isEmpty) {
        sys.error(s"Cannot parse value <$nextArg> for expected argument <${expectedArgs.head}>")
      }
      expectedArgs = expectedArgs.tail
    }

    if(expectedArgs.nonEmpty) {
      sys.error(s"Missing argument value for ${expectedArgs.head}")
    }

  }

}

class StringArgDef(val name: String, val description: String) extends ArgDef[String] {
  override def parseValue(v: String): Option[String] = Some(v)
}

class IntArgDef(val name: String, val description: String) extends ArgDef[Int] {
  override def parseValue(v: String): Option[Int] = {
    try {
      Some(v.toInt)
    } catch {
      case (_: Exception) => None
    }
  }
}
