package dk.edutor.eduport


abstract class Challenge(val id: Int, val description: String, val tags: List<String>)

class StringChallenge(
    id: Int,
    description: String,
    tags: List<String>,
    val question: String
    ) : Challenge(id, description, tags)


class MCChallenge(
    id: Int,
    description: String,
    tags: List<String>,
//  answers have a string question and boolean: right question
    val question: String,
    val answers: Map<String,Boolean>
    ) : Challenge(id, description, tags)

