package dk.edutor.eduport.webchecker

import java.io.File

class DBWebChallenge
{
    var webChallengeId : Int = 0
    var webChallengeName : String = ""
    var webChallengeType : String = ""
    var webChallengeFileName : String = ""
    lateinit var webChallengeFile : File
}