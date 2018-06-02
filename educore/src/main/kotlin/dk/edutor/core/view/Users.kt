package dk.edutor.core.view

import dk.edutor.eduport.User

open class UserIdentifier(val id: Int)

class UserSummary(id: Int, val code: String, val name: String) : UserIdentifier(id)

fun User.toSummary() = UserSummary(id, code, name)

fun User.toIdentifier() = UserIdentifier(id)

