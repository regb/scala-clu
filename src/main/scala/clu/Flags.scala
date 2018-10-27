package clu

abstract class FlagDef[A] {

  val name: String
  val description: String
  val default: Option[A]
  val short: Option[Char]

  // TODO: required? Does it make sense to have required flags?

  private var value_ : Option[A] = None

  def value: Option[A] = value_.orElse(default)

  def parseAndSetValue(v: String): Option[A] = {
    value_ = parseValue(v)
    value_
  }

  def parseValue(v: String): Option[A]

  /** Parse one single arg from the args list.
    *
    * Returns the parsed value if it matches the name and value as in --NAME=VALUE, or
    * Some(None) if it matches just the name as in --NAME VALUE, with VALUE being the
    * next arg.
    */
  def parse(arg: String): Option[Option[A]] = {
    val prefix = s"--$name"

    if(arg == prefix) {
      Some(None)
    } else if(arg.startsWith(s"${prefix}=")) {
      val v = arg.substring((s"${prefix}=").length)
      parseAndSetValue(v).map(v => Some(v))
    } else if(short.exists(c => arg == s"-$c")) {
      Some(None)
    } else {
      None
    }
  }
}

object FlagDef {

  /** Checks if the arg is a syntactically correct flag.
    *
    * This does not guarantee that the arg is a valid flag
    * for the command, but it checks that the arg looks like
    * a flag.
    */
  def isSyntacticFlag(arg: String): Boolean = {
    arg.startsWith("-")
  }

  ///** Process the list of arguments and set all corresponding flags. */
  //def processFlags[_](flags: List[FlagDef[_]])(args: List[String]): Unit = args match {
  //  case a :: as => {

  //    def rec(fs: List[FlagDef[_]]): Option[(FlagDef[_], List[String])] = fs match {
  //      case flag :: rest => {
  //        flag.parse(a) match {
  //          case None => {
  //            rec(rest)
  //          }
  //          case Some(None) => {
  //            as match {
  //              case v :: as => {
  //                flag.parseAndSetValue(v) match {
  //                  case Some(v) => {
  //                    Some((flag, as))
  //                  }
  //                  case None => {
  //                    println("Oups, value <" + v + "> cannot be parsed!")
  //                    throw new RuntimeException
  //                  }
  //                }
  //              }
  //              case Nil => {
  //                println("Oups, missing value!")
  //                throw new RuntimeException
  //              }
  //            }
  //          }
  //          case Some(v) => {
  //            Some((flag, as))
  //          }
  //        }
  //      }
  //      case Nil => None
  //    }
  //    val flag = rec(flags)
  //    flag match {
  //      case None =>
  //        println("No flag matching: " + a)
  //        processFlags(flags)(as)
  //      case Some((f, as)) =>
  //        processFlags(flags)(as)
  //    }
  //  }
  //  case Nil => ()
  //}

  /** Process the list of arguments and set all corresponding flags.
    *
    * Move into the args until we reach the end of the flags. In case
    * of error, print the error message and quit.
    */
  def processFlags[_](flags: List[FlagDef[_]])(args: ArraySlice[String]): Unit = {
    while(args.hasNext && isSyntacticFlag(args.head)) {
      def rec(fs: List[FlagDef[_]]): Option[FlagDef[_]] = fs match {
        case flag :: rest => {
          flag.parse(args.head) match {
            case None => {
              rec(rest)
            }
            case Some(None) => {
              args.next()
              if(!args.hasNext) {
                sys.error(s"Missing flag value for flag: $flag")
              }
              val value = args.next()
              flag.parseAndSetValue(value) match {
                case None =>
                  sys.error(s"Cannot parse value <$value> for flag <$flag>")
                case Some(v) =>
                  Some(flag)
              }
            }
            case Some(_) => {
              args.next()
              Some(flag)
            }
          }
        }
        case Nil => None
      }

      if(rec(flags).isEmpty) {
        sys.error(s"Unknown flag: ${args.head}")
      }
    }
  }
}

class IntFlagDef(val name: String, val description: String, val default: Option[Int], val short: Option[Char] = None) extends FlagDef[Int] {

  override def parseValue(v: String): Option[Int] = {
    try {
      Some(v.toInt)
    } catch {
      case (_: Exception) => None
    }
  }

}
class StringFlagDef(val name: String, val description: String, val default: Option[String], val short: Option[Char] = None) extends FlagDef[String] {
  override def parseValue(v: String): Option[String] = Some(v)
}
