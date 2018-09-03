package dk.edutor.edutool.markdown

import dk.edutor.util.Path
import dk.edutor.util.pathOf
import org.junit.Assert.*
import org.junit.Test

class ParserTest {

  @Test
  fun testMatchesHeading1() {
    assertTrue("# Overskrift".matches(specialRegex))
    }

  @Test
  fun testMatchesHeading2() {
    assertTrue(" ## Underoverskrift".matches(specialRegex))
    }

  @Test
  fun testMatchesListingAsterix() {
    assertTrue("  * et punkt".matches(specialRegex))
    }

  @Test
  fun testMatchesListingHyphen() {
    assertTrue("- et andet punkt".matches(specialRegex))
    }

  @Test
  fun testMatchesListingOrdered() {
    assertTrue("      45. et ordnet punkt".matches(specialRegex))
    }

  @Test
  fun testNormaliseParagraphLines() {
    val textToNormalise = pathOf("Dette er et", "simpelt afsnit på", "tre linier", "")
    val normalisedText = normalise(Line(0, ""), textToNormalise)
    assertEquals("Dette er et simpelt afsnit på tre linier", normalisedText.head.text)
    }

  @Test
  fun testNormaliseTwoHeadings() {
    val textToNormalise = pathOf("  ## Overskrift", "# En større overskrift", "på to linier", "")
    val normalisedText = normalise(Line(0, ""), textToNormalise)
    assertEquals("## Overskrift", normalisedText.head.text)
    assertEquals("# En større overskrift på to linier", normalisedText.tail?.head?.text)
    }

  @Test
  fun testNormaliseTwoParagraphs() {
    val textToNormalise = pathOf("Dette er et", "simpelt afsnit på", "tre linier", "", "Dette er det", "næste på to")
    val normalisedText = normalise(Line(0, ""), textToNormalise)
    assertEquals("Dette er et simpelt afsnit på tre linier", normalisedText.head.text)
    assertEquals("Dette er det næste på to", normalisedText.tail?.head?.text)
    }


  }
