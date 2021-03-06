#!/usr/bin/env bash

sudo apt-get update
sudo debconf-set-selections <<< 'mysql-server mysql-server/root_password password root'
sudo debconf-set-selections <<< 'mysql-server mysql-server/root_password_again password root'
sudo apt-get install -y vim curl python-software-properties
sudo apt-get update
sudo apt-get -y install mysql-server
sed -i "s/^bind-address/#bind-address/" /etc/mysql/my.cnf
mysql -u root -proot -e "GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY 'Edutor@2018' WITH GRANT OPTION; FLUSH PRIVILEGES; SET GLOBAL max_connect_errors=10000; create database edutor;"
sudo /etc/init.d/mysql restart
echo "mysql -uroot -proot edutor" > start_db
chmod 755 start_db
