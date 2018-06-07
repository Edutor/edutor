# Edutor Core Application Server

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
1. `/challenge/7` to get details for challenge #7
* `/challenge/Java` to get summaries for all challenges tagged `Java`
1. Testin items
