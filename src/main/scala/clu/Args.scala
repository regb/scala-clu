package clu

abstract class ArgDef[A] {

  val name: String
  val description: String

}

class StringArgDef(val name: String, val description: String) extends ArgDef[String] {

}

class IntArgDef(val name: String, val description: String) extends ArgDef[Int] {

}
