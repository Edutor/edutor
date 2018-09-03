package dk.edutor.edutool.markdown

import org.hamcrest.CoreMatchers.`is`
import org.junit.Test
import org.junit.Assert.*

class ConstructFromTest {

  @Test
  fun constructSimple() {
    val text = "Hello World!"
    val result = constructFrom(text)
    assertThat(
        "simple text shall construct only one Text",
        result.size,
        `is`(1))
    assertThat(
        "First and only member has original text",
        result[0].text,
        `is`(text)
        )
    }

  @Test
  fun constructSimpleOneLevelBold() {
    val text = "Hello **great** world"
    val result = constructFrom(text)
    assertThat(
        "One level tree with one bold subtext should give three Text elements",
        result.size,
        `is`(3))
    val t1 = result[1]
    assertThat(
        "Second member has bolded text",
        t1.text,
        `is`("great")
        )
    assertThat(
        "Second member has BOLD modifier",
        t1.modifier,
        `is`(Text.Modifier.BOLD)
        )
    }

  @Test
  fun constructSimpleOneLevelAllBold() {
    val text = "**Hello great world**"
    val result = constructFrom(text)
    assertThat(
        "One level tree with all bold text should give one Text element",
        result.size,
        `is`(1))
    val t0 = result[0]
    assertThat(
        "First member has all text",
        t0.text,
        `is`("Hello great world")
        )
    assertThat(
        "First member has BOLD modifier",
        t0.modifier,
        `is`(Text.Modifier.BOLD)
        )
    }


  }