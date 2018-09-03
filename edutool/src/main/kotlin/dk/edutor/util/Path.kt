package dk.edutor.util

import kotlin.coroutines.experimental.buildIterator


class Path<T>(val head: T, val tail: Path<T>?) : Iterable<T> {

  override fun toString(): String = "$head -> ${tail?.toString()}"

  override fun iterator() = buildIterator {
    yield(head)
    if (tail != null) yieldAll(tail)
    }

  }

fun <T> pathFrom(iterator: Iterator<T>): Path<T>? =
    if (iterator.hasNext()) Path(iterator.next(), pathFrom(iterator))
    else null

fun <T> pathOf(items: Iterable<T>) = pathFrom(items.iterator())

fun <T> pathOf(vararg items: T) = pathFrom(items.iterator())

fun main(args: Array<String>) {
  val path = pathOf(listOf(7, 9, 13))
  val path2 = pathOf(7, 9, 13, 4711)
  println(path)
  println(path2)
  }