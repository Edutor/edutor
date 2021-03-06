package dk.edutor.core.view.markdown

import com.google.gson.GsonBuilder

abstract class Content {
  abstract val type: String
  }

interface Container {
  fun level(): Int
  fun append(content: Content)
  }

class Text(val value: String) : Content() {
  override val type = "TEXT"
  }

open class Section(
    val title: String,
    val level: Int = 0,
    vararg children: Content
    ) : Content(), Container {
  override val type = "SECTION"
  val contents = mutableListOf<Content>()

  init {
    contents.addAll(0, children.asList())
    }

  override fun level() = level

  override fun append(content: Content) {
    contents.add(content)
    }
  }

class Document(val id: Int, title: String, vararg children: Content) : Section(title, 0, *children)

class Query(val query: String) : Content() {
  override val type = "QUERY"
  }


class Choice(val test: String, vararg var contents: Content) : Content() {
  override val type = "CHOICE"
  }

class ItemList(@Transient val level: Int) : Content(), Container {
  override val type = "LIST"
  val contents = mutableListOf<Content>()

  override fun level() = level

  override fun append(content: Content) {
    contents.add(content)
    }

  }

fun main(args: Array<String>) {
  val template = """
  # Headline one
  Some text on two lines
  this is the second line

  !QUERY 3

     Another afsnit
  også på to linier

   * First
   * Second
     - and a half
     - and two thirds
   * Third

  # Headline two
  Here is another list:
  + en
  + to
  + tre
  ### And three
  """.trimIndent()

  val builder = DocumentBuilder(7, "Testing")
  val lines = builder.parse(template)
  lines.forEach { builder.append(it) }
  val document = builder.document
  val gson = GsonBuilder().setPrettyPrinting().create()

  println(gson.toJson(document.contents))
  }