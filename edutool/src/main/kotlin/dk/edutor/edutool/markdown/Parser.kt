package dk.edutor.edutool.markdown

import dk.edutor.util.Path

interface Node

data class Text(val text: String, val modifier: Modifier = Modifier.NORMAL) : Node {
  enum class Modifier(val code: Char) {
    NORMAL(' '),
    BOLD('b'),
    ITALIC('i'),
    CODE('c')
    }
  }

data class Paragraph(val content: List<Text>, val level: Int = 0) : Node

data class ItemList(val content: List<Node>, val numbered: Boolean = false, val indentation: Int = 0) : Node

data class Listing(val content: List<String>, val language: String = "text") : Node

data class Challenge(val query: String, val test: String) : Node

data class Condition(val test: String) : Node


val specialRegex = Regex("""^ *((#+)|([*\-])|(\d+\.)) .*""")
val textRegex = Regex("""\*\*(.*)\*\*|__(.*)__|\*(.*)\*|_(.*)_|`(.*)`_""", RegexOption.CANON_EQ)

data class Line(val indent: Int, val text: String) {
  operator fun plus(suffix: String) = Line(indent, text+" "+suffix)
  fun trim() = Line(indent, text.trim())
  val empty: Boolean get() = text.trim().isEmpty()
  }

fun normalise(lineSoFar: Line, lines: Path<String>?): Path<Line> {
  if (lines == null) return Path(lineSoFar.trim(), null)
  var text = lines.head.trimStart()
  val indent = lines.head.length - text.length
  if (text.matches(specialRegex) || text.isBlank()) {
    if (lineSoFar.empty) return normalise(Line(indent, text), lines.tail)
    else return Path(lineSoFar.trim(), normalise(Line(indent, text), lines.tail))
    }
  return normalise(lineSoFar+text, lines.tail)
  }

fun constructFrom(line: String, textsSoFar: MutableList<Text> = mutableListOf()) : List<Text> {
  fun addIfNotEmpty(text: Text): List<Text> {
      if (text.text.isNotEmpty()) textsSoFar.add(text)
      return textsSoFar
      }

  val matches = textRegex.findAll(line)
  if (matches.count() == 0) return addIfNotEmpty(Text(line))

  var index = 0
  matches.forEach { match ->
    addIfNotEmpty(Text(line.substring(index, match.range.first)))
    index = match.range.endInclusive + 1
    for (groupIndex in 1 until match.groups.size) {
      val group = match.groups[groupIndex] ?: continue
      when (groupIndex) {
        1,2 -> addIfNotEmpty(Text(group.value, Text.Modifier.BOLD))
        3,4 -> addIfNotEmpty(Text(group.value, Text.Modifier.ITALIC))
        5   -> addIfNotEmpty(Text(group.value, Text.Modifier.CODE))
        }
      break
      }
    }
  addIfNotEmpty(Text(line.substring(index)))
  return textsSoFar
  }


/*

# Dette er en overskrift

Dette er en **fed** og _kursiv_
tekst over flere
linier



 */