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

### Chosen Endpoints

#### To add new product:
POST https://coin-collector-app.herokuapp.com/{product_name}/{my_price}
POST https://coin-collector-app.herokuapp.com/STANDARD/6
Body:
{
    "1.0": 20.0,
    "5.0": 16.0,
    "10.0": 14.0,
    "25.0": 10.0,
    "50.0": 8.0
}
![image](https://user-images.githubusercontent.com/44747531/122130747-62711280-ce38-11eb-82fb-2cdee8eed557.png)

#### To add new sale:
POST coin-collector-app.herokuapp.com/sale/{productName}/{mySortPrice}/{quantity}/{personName}/{discount}  (discount is optional)
POST coin-collector-app.herokuapp.com/sale/{productName}/{mySortPrice}/{quantity}/{personName}
POST https://coin-collector-app.herokuapp.com/sale/STANDARD/6/3/Marek/10
![image](https://user-images.githubusercontent.com/44747531/122131244-2be7c780-ce39-11eb-90d9-7613ef7081d3.png)

#### Get earnings by chosen period:
GET coin-collector-app.herokuapp.com/ale/period/{dateStart}/{dateEnd}
GET https://coin-collector-app.herokuapp.com/sale/period/2021-06-13/2021-06-16
![image](https://user-images.githubusercontent.com/44747531/122133698-808d4180-ce3d-11eb-9c0e-ba7fe5e8b6f6.png)






