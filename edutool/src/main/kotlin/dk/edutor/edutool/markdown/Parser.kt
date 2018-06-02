package dk.edutor.edutool.markdown

class Document {
  val elements = mutableListOf<Element>()
  }

abstract class Element

abstract class InlineElement : Element()

abstract class BlockElement : Element()

class Sentence : InlineElement() {
  val parts = mutableListOf<InlineElement>()
  }

sealed class Text(val value: String) : InlineElement() {
  class Emphasized(value: String) : Text(value)
  class Italic(value: String) : Text(value)
  class Code(value: String) : Text(value)
  }

class Heading(val sentence: Sentence, val level: Int) : BlockElement()

class Paragraph(val sentence: Sentence) : BlockElement()

