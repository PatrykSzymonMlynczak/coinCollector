# CoinCollector
## Table of contents
* [Why this version was suspended](#why-this-version-was-suspended)
* [General info](#general-info)
* [Versions](#versions)
* [Technologies](#technologies)
* [How it works](#how-it-works)
* [Status](#status)


## Why this version was suspended
Because Heroku is re-started, and any saved data on the server is removed I wanted to send it to my own Google Drive. 
Unfortunately, to find the file on GDrive its name is not enough. You need its Id, which is changing after each re-write data, and that leads to the same problem :
Can't save file id on Heroku because it will be removed. 
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

## Status
Suspended 

## How it works
Application was performed mostly for CBD sales, and is used by me, but Im working on local-machine-version.
Base endpoint https://coin-collector-app.herokuapp.com/

###Chosen Endpoints

####To add new product:
POST base-url/{product_name}/{my_price}
as a body pass map {quantity:price_per_gram}
![image](https://user-images.githubusercontent.com/44747531/122130747-62711280-ce38-11eb-82fb-2cdee8eed557.png)

####To add new sale:
POST base-url/sale/{productName}/{mySortPrice}/{quantity}/{personName}/{discount}  (discount is optional)
POST base-url/sale/{productName}/{mySortPrice}/{quantity}/{personName}
![image](https://user-images.githubusercontent.com/44747531/122131244-2be7c780-ce39-11eb-90d9-7613ef7081d3.png)

####Get earnings by chosen period:
GET base-url/sale/period/{dateStart}/{dateEnd}
![image](https://user-images.githubusercontent.com/44747531/122133698-808d4180-ce3d-11eb-9c0e-ba7fe5e8b6f6.png)






