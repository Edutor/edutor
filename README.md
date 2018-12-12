# Edutor Core Application Server

### Start the server
1. start the vagrant virtual server to have the mysql server running (see guide below)
2. Open educore project and run the file: src/main/kotlin/Edutor.kt
3. In browser go to: localhost:8080/run-only-once then go to localhost:8080/challenge
  - All challenges should appear now as json.
4. To recreate the database: change the version number in Edutor.kt `val db = MySqlManager(version = 4)`
## Deploying

### MySql

To install and/or run MySql server in Vagrant:
Start a terminal in the `edudata` folder and write:

```bash
edudata$ vargrant up
```

The server can be stopped with:

```bash
edudata$ vagrant halt
```

To access the server:
```bash
ududata$ vagrant ssh
vagrant@edudata:~$ mysql -uroot -p
Password: _
```

Write the password

## REST

* `/challenge` to get summaries for all challenges
* `/challenge/1` to get details for challenge #1
* `/challenge/Java` to get summaries for all challenges tagged `Java`

* `/quest/7` to get quest #7

* `/query/4` to get details for challenge #4

* `/choice/true` to get the answer `true`
* `/choice/false` to get the answer `false`

### Examples
```
http://localhost:8080/challenge/3
---------------------------------
{
    "description": "Every year ...",
    "question": "Who is not IT-Seminar university",
    "options": [
        "Haaga-Helia",
        "ITU",
        "Hesso-Valais",
        "UE"
    ],
    "portKey": "MULTIPLE_CHOICE",
    "template": "checkbox",
    "id": 3,
    "dtype": "CHOICE"
}
```

```
http://localhost:8080/query/3
---------------------------------
{
    "description": "Every year ...",
    "question": "Who is not IT-Seminar university",
    "options": [
        "Haaga-Helia",
        "ITU",
        "Hesso-Valais",
        "UE"
    ],
    "portKey": "MULTIPLE_CHOICE",
    "template": "checkbox",
    "id": 3,
    "dtype": "CHOICE"
    "status": {
      "answers" : [0,1],
      "explanation": "Some right things here",
      "grade": 60
    }
}
```


```
http://localhost:8080/evaluate/choice/3
-------------------------------------
{
    "answers" : [
        "Haaga-Helia",
        "Hesso-Valais",]
}
-------------------------------------
{
    "explanation": "Some right things here",
    "grade": 60
}
```

# Example flow

Create user
```
POST /user
{ "code": "chu", "name": "Caroline Hundahl" }
```

```
{
    "id": 16,
    "code": "chu",
    "name": "Caroline Hundahl"
}
```

Login as `chu`
```
POST /login/chu
```

```
{
    "id": 16,
    "code": "chu",
    "name": "Caroline Hundahl"
}
```

Create template
Select in browser:
```
http://localhost:8080
```
Chose file (simple)

```markdown
6: Example with typos
# This is an example quest

This quest only uses sections tex, and quests.
Texts can span mulitiple lines.
QUERY (or QUESTION if you like) uses markdown preprocessor syntax.

## Sections can be in different levels

First question is the challenge with is #3

!QUERY 3

!QUESTION 4

Second question uses #4

# More sections in different levels apply.

!QUERI 5

And a misspelled entry just creates a text
```

Get Quest
```
GET /quest/6
```

```json
{
    "id": 6,
    "type": "SECTION",
    "title": "Example with typos",
    "level": 0,
    "contents": [
        {
            "type": "SECTION",
            "title": "This is an example quest",
            "level": 1,
            "contents": [
                {
                    "type": "TEXT",
                    "value": "This quest only uses sections tex, and quests. Texts can span mulitiple lines. QUERY (or QUESTION if you like) uses markdown preprocessor syntax."
                },
                {
                    "type": "SECTION",
                    "title": "Sections can be in different levels",
                    "level": 2,
                    "contents": [
                        {
                            "type": "TEXT",
                            "value": "First question is the challenge with is #3"
                        },
                        {
                            "type": "QUERY",
                            "query": "3"
                        },
                        {
                            "type": "QUERY",
                            "query": "4"
                        },
                        {
                            "type": "TEXT",
                            "value": "Second question uses #4"
                        }
                    ]
                }
            ]
        },
        {
            "type": "SECTION",
            "title": "More sections in different levels apply.",
            "level": 1,
            "contents": [
                {
                    "type": "TEXT",
                    "value": "!QUERI 5 - unknown control sequence QUERI"
                },
                {
                    "type": "TEXT",
                    "value": "And a misspelled entry just creates a text"
                }
            ]
        }
    ]
}
```

Get the first (3) challenge:
```
GET /query/3
```

```json
{
    "challenge": {
        "description": "Third challenge",
        "question": "Who is not IT-Seminar university",
        "answers": [
            "Haaga-Helia",
            "ITU",
            "Hesso-Valais",
            "UE"
        ],
        "portKey": "MULTIPLE_CHOICE",
        "template": "checkbox",
        "id": 3,
        "dtype": "CHOICE"
    }
}
```

Answer challenge 3 incorrectly:
```
POST /evaluate/CHOICE
{ "challenge": { "id": 3, "dtype" : "CHOICE" }, "answers": [1,3] }
```

```
{
    "challenge": {
        "description": "Third challenge",
        "question": "Who is not IT-Seminar university",
        "answers": [
            "Haaga-Helia",
            "ITU",
            "Hesso-Valais",
            "UE"
        ],
        "portKey": "MULTIPLE_CHOICE",
        "template": "checkbox",
        "id": 3,
        "dtype": "CHOICE"
    },
    "solution": {
        "answers": [
            1,
            3
        ],
        "challenge": {
            "id": 3,
            "dtype": "CHOICE"
        },
        "solver": {
            "code": "chu",
            "name": "Caroline Hundahl",
            "id": 16
        },
        "grade": 60,
        "explanation": "Some right things here",
        "id": 0
    }
}
```
