#!/usr/bin/env bash

echo "**********EXPORTING DEBIAN_FRONTEND**********"
export DEBIAN_FRONTEND="noninteractive";

echo "**********INSTALLING DEBCONF**********"
sudo apt-get update
sudo apt-get install -y debconf-utils

echo "**********INSTALLING VIM**********"
sudo apt-get install -y vim

echo "**********INSTALLING CURL**********"
sudo apt-get install -y curl

echo "**********SETTING DEBCONF**********"
sudo debconf-set-selections <<< 'mysql-apt-config mysql-apt-config/select-server select mysql-8.0'
sudo debconf-set-selections <<< 'mysql-apt-config mysql-apt-config/select-product select Ok'
sudo debconf-set-selections <<< 'mysql-server mysql-server/root_password password root'
sudo debconf-set-selections <<< 'mysql-server mysql-server/root_password_again password root'
sudo debconf-set-selections <<< 'mysql-community-server mysql-community-server/root-pass password root'
sudo debconf-set-selections <<< 'mysql-community-server mysql-community-server/re-root-pass password root'

echo "**********GETTING DEBCONF**********"
wget -c https://dev.mysql.com/get/mysql-apt-config_0.8.12-1_all.deb
sudo -E dpkg -i mysql-apt-config_0.8.12-1_all.deb
#sudo rm mysql-apt-config_0.8.12-1_all.deb

echo "**********UPDATING APT**********"
sudo apt-get update

echo "**********INSTALLING MYSQL**********"
sudo -E apt-get install -y mysql-server
sudo sed -i "s/^bind-address/#bind-address/" /etc/mysql/my.cnf
echo "bind-address = 127.0.0.1" | sudo tee -a /etc/mysql/conf.d/mysql.cnf

echo "**********INITIALIZING MYSQL**********"
sudo mysql -uroot -proot -e "CREATE USER 'edutor'@'localhost' IDENTIFIED BY 'Edutor@2019'; CREATE USER 'edutor'@'%' IDENTIFIED BY 'Edutor@2019'; GRANT ALL PRIVILEGES ON *.* TO 'edutor'@'localhost' WITH GRANT OPTION; GRANT ALL PRIVILEGES ON *.* TO 'edutor'@'%' WITH GRANT OPTION; FLUSH PRIVILEGES; SET GLOBAL max_connect_errors=10000; create database edutor;"

echo "**********RESTARTING MYSQL**********"
sudo service mysql restart
# echo "mysql -uroot -proot edutor" > start_db
# chmod 755 start_db
