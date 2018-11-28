package dk.edutor.core.view.markdown

open abstract class MdContent {
  abstract val type: String
  var parent: MdSection? = null
  }

class MdText(val value: String) : MdContent() {
  override val type = "TEXT"
  }

class MdSection(val title: MdText, val level: Int = 1, vararg children: MdContent) : MdContent() {
  override val type = "SECTION"
  val contents = mutableListOf<MdContent>()
  init {
    for (child in children) add(child)
    }
  fun add(content: MdContent) {
    content.parent = this
    contents.add(content)
    }
  }

class MdQuery(val query: String) : MdContent() {
  override val type = "QUERY"
  }


class MdChoice(val test: String, vararg var contents: MdContent) : MdContent() {
  override val type = "CHOICE"
  }

data class Line(val indent: Int, val text: String) {
  val isEmpty: Boolean
    get() = indent == 0 && text.isEmpty()
  val isNewContent: Boolean
    get() = text.startsWith('#') && indent == 0 || text.startsWith("{") || isEmpty
  }


fun parse(template: String): MdSection {
  val rawLines = template.lines().map { Line(it.indexOfFirst { c -> c != ' ' && c != '\t'}, it.trim())}
  val lines = mutableListOf<Line>()
  var line = Line(0, "")
  for (rawLine in rawLines) {
    if (line.isEmpty) {
      line = rawLine
      continue
      }
    if (rawLine.isNewContent) {
      lines.add(line)
      line = rawLine
      continue
      }
    line = line.copy(text = "${line.text} ${rawLine.text}")
    }
  var section = MdSection(MdText("Root"), 0)
  for (line in lines) {

    }


  return MdSection(MdText("Document"), 1)
  }