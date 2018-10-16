package dk.edutor.edutool.markdown

fun main(args: Array<String>) {
  val boldRegex = Regex("""\*\*(.*)\*\*|__(.*)__|\*(.*)\*|_(.*)_|`(.*)`_""") //, RegexOption.CANON_EQ)
  val text =  "dette er **fed `skrift` med noget *kursiv (__fedt__ nok)* i**, såmænd og __lidt__ mere"
  val text2 = "dette er __fed `skrift` med noget *kursiv (__fedt__ nok)* i__, såmænd"
  val match = boldRegex.find(text2)
  println(match?.groupValues)
  val group0 = match?.groups?.get(0)?.range
  val group2 = match?.groups?.get(2)?.range
  println("$group0  $group2")
  println("---")
  val matches = boldRegex.findAll(text)
  matches.forEach {
    println(it.groupValues)
    println(it.range)
    it.groups.forEach { println("  $it") }
    }

  println(constructFrom(text))
  }