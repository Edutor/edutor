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
