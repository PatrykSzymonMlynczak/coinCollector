# CoinCollector
## Table of contents
* [General info](#general-info)
* [Versions](#versions)
* [Technologies](#technologies)
* [Setup](#setup)
* [How it works](#how-it-works)
* [To Do](#to-do)
* [Status](#status)


## General info
The application's goal is to help with managing my personal home budget. 
Created for control income from sales and services which I'm providing to earn money. 
For sake of the second aim which is to develop programming skills, 
and joy from coding instead of connecting to the database I decided to base data on Collections and save it as a JSON to files.


## Versions
Here on the master branch is the version which saves data to my personal Google Drive, and it works on Heroku. 
For more info check this : <br />
[HEROKU VERSION](https://github.com/PatrykSzymonMlynczak/coinCollector/tree/master)


## Technologies:
* Java 11
* Spring: 
  * Boot
  * Web
* Mapstruct
* Gradle 
* REST
* Lombok
* Swagger


## Setup 
To Deploy Application on your computer, you need Java 11+ and Gradle:

1. Pull the project
3. Command Line:
>gradle clean build

3. Run .jar file with command:
>java -jar patch\file_name.jar

## How it works
Application was performed mostly for CBD sales, and is used by me. <br /> 
Photos are taken from [HEROKU VERSION](https://github.com/PatrykSzymonMlynczak/coinCollector/tree/master)<br /> 
Actual version includes Swagger, to check out run project locally [Setup](#setup) <br />
and hit [SWAGGER UI](http://localhost:8080/swagger-ui.html#/)


### Chosen Endpoints

#### To add new product:
POST localhost:8080/{product_name}/{my_price}<br /> 
POST localhost:8080/STANDARD/6<br /> 
Body:<br />
{"1.0": 20.0,<br />
"5.0": 16.0,<br />
"10.0": 14.0,<br />
"25.0": 10.0,<br />
"50.0": 8.0}<br />
![image](https://user-images.githubusercontent.com/44747531/122130747-62711280-ce38-11eb-82fb-2cdee8eed557.png)

#### To add new sale:
POST localhost:8080/sale/{productName}/{mySortPrice}/{quantity}/{personName}/{discount}  (discount is optional)<br /> 
POST localhost:8080/sale/{productName}/{mySortPrice}/{quantity}/{personName}<br /> 
POST localhost:8080/sale/STANDARD/6/3/Marek/10
![image](https://user-images.githubusercontent.com/44747531/122131244-2be7c780-ce39-11eb-90d9-7613ef7081d3.png)

#### Get earnings by chosen period:
GET localhost:8080/sale/period/{dateStart}/{dateEnd}<br /> 
GET localhost:8080/sale/period/2021-06-13/2021-06-16
![image](https://user-images.githubusercontent.com/44747531/122133698-808d4180-ce3d-11eb-9c0e-ba7fe5e8b6f6.png)

## To Do
* HEXAGONAL ARCHITECTURE  
    * REFACTOR, 
    * MAKE MODULES, 
    * EXTRACT IM-MEMORY-REPO FROM BUSINESS LOGIC  
* EXPENSES CONTROLLER
    * MANUAL ADDING EXPENSES
* CLIENT CONTROLLER + DB
* ADAPT TO CONNECT WITH A DATABASE ??
* FRONTEND
* ADAPT GITFLOW PROD-DEV-TEST
* DOCKERIZE IT

## Status
Constantly developed
