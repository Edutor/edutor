package dk.edutor.core.view

interface MdContent {
  val type: String
  }

class MdText(val value: String) : MdContent {
  override val type = "TEXT"
  }

class MdSection(val title: MdText, vararg var contents: MdContent) : MdContent {
  override val type = "SECTION"
  }

class MdQuery(val query: String) : MdContent {
  override val type = "QUERY"
  }


class MdChoice(val test: String, vararg var contents: MdContent) : MdContent {
  override val type = "CHOICE"
  }
