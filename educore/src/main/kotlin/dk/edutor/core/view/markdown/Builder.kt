package dk.edutor.core.view.markdown

import java.util.*

data class Line(val indent: Int, val text: String) {
  val isEmpty = text.isEmpty()
  val startsLine: Boolean
    get() = text.startsWith('#') && indent == 0
         || text.startsWith("*")
         || text.startsWith("-")
         || text.startsWith("+")
         || text.startsWith("!")
         || isEmpty
  val endsLine = text.startsWith("#") && indent == 0
  }

class DocumentBuilder {
  val document = Document()
  val stack = Stack<Container>()

  init {
    stack.push(document)
    }

  fun append(line: Line) {
    when (line.text[0]) {
      '#' -> {
        val level = line.text.indexOfFirst { it != '#' }
        val section = Section(Text(line.text.substring(level).trim()), level)
        while (stack.peek().level() >= level) stack.pop()
        stack.peek().append(section)
        stack.push(section)
        }
      '*', '-', '+' -> {
        val level = line.indent + 1000
        val text = line.text.substring(1).trim()
        while (stack.peek().level() > level) stack.pop()
        val top = stack.peek()
        when (top) {
          is ItemList -> {
            if (top.level() == level) top.append(Text(text))
            else {
              val list = ItemList(level)
              list.append(Text(text))
              top.append(list)
              stack.push(list)
              }
            }
          else -> {
            val list = ItemList(level)
            list.append(Text(text))
            top.append(list)
            stack.push(list)
            }
          }
        }
      '!' -> {
        val text = line.text.substring(1)
        val parts = text.split(" +".toRegex(), limit = 2)
        val top = stack.peek()
        if (parts.size < 2) top.append(Text(line.text))
        else {
          when (parts[0]) {
            "QUERY", "QUESTION" -> {
              val query = Query(parts[1])
              top.append(query)
              }
            else -> {
              TODO("Implement links")
              }
            }
          }
        }
      else -> {
        val text = Text(line.text)
        stack.peek().append(text)
        }
      }
    }

  fun parse(template: String): List<Line> {
    val rawLines = template.lines().map {
      Line(it.indexOfFirst { c -> !c.isWhitespace() }, it.trim())
      }
    val lines = mutableListOf<Line>()
    var line: Line? = null
    fun addLine(l: Line?) {
      if (l == null || l.isEmpty) return
      lines.add(l)
      }
    for (rawLine in rawLines) {
      println(rawLine)
      if (rawLine.startsLine) {
        addLine(line)
        line = rawLine
        }
      else {
        if (line == null || line.isEmpty) line = rawLine
        else line = Line(line.indent, "${line.text} ${rawLine.text}")
        }
      if (rawLine.endsLine) {
        addLine(line)
        line = null
        }
      }
    if (line != null) lines.add(line)
    println("---")
    lines.forEach { println(it) }
    return lines
    }

  }

fun parse(template: String): Document {
  val builder = DocumentBuilder()
  builder.parse(template).forEach { builder.append(it) }
  return builder.document
  }
