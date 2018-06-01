package dk.edutor.core.model

import dk.edutor.core.model.db.CHALLENGES
import dk.edutor.core.model.db.MySqlManager

fun main(args: Array<String>) {
  val db = MySqlManager(1)
  for (challenge in CHALLENGES) println(challenge)


  }