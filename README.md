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
