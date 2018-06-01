package dk.edutor.eduport

interface Port {
  fun sayHello(text: String) : String
  fun check(solution: Solution): Assessment
  }

open class Person(val id: Long)

open class Assessment(val solution: Solution, val grade: Double)

