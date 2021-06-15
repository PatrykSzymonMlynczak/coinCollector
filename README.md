# CoinCollector
## Table of contents
* [Why this version was suspended](#why-this-version-was-suspended)
* [General info](#general-info)
* [Versions](#versions)
* [Technologies](#technologies)
* [Setup](#setup)
* [Status](#status)


## Why this version was suspended
Because Heroku is re-started, and any saved data on the server is removed I wanted to send it to my own Google Drive. 
Unfortunately, to find the file on GDrive its name is not enough. You need its Id, which is changing after each re-write data, and that leads to the same problem :
Can't save id on Heroku because it will be removed. 
Besides that this implementation of Google API enforces change Tomcat to Jetty and I think that could be problematic


## General info
The application's goal is to help with managing my personal home budget. 
Created for control income from sales and services which I'm providing to earn money. 
For sake of the second aim which is to develop programming skills, 
and joy from coding instead of connecting to the database I decided to base data on Collections and save it as a JSON to files.


## Versions
Here on the default branch is developed version 
For more info check this : [DEVELOPED VERSION](https://github.com/PatrykSzymonMlynczak/coinCollector/tree/withoutGDrive)


## Technologies:
* Java 11
* Spring: 
  * Boot
  * Web
* Gradle 
* REST
* Google API
* Circle CI
* Heroku


## Setup


## Status
Contantly developed
