# Air Traffic Controller System
## Overview
A OOPS design for ATC (Air Traffic Controller) which control the flights coming to and going from Airport and assign them to the available gate based on there size.

 
## Scope
Scope is limited to design two Main controller class which will manage main two entities in the System, which are Gate and Flight respectively.

##Limitation
Currently only provide two interfaces to interact with AirPort Systems, i.e. (AdminAirportController and AirTrafficController). 
  
## Admin Controller Use case
##### 1. Addition of New Gate
##### 2. Removal of Gate if it is not occupied by the flight
##### 3. Move Gate to Maintenance Mode and Vice versa provided it is not occupied by the flight
##### 4. Given a Gate ID return Gate Info
## Ait Traffic Controller Use case
##### 1. Assign the flight to next available gate
##### 2. De Assign flight from Gate and Make the gate available for next flight

## Prerequisite
##### 1. Maven 3.x
##### 2. JDK 1.8

## Build Process
##### 1. Get the code from github https://github.com/deyindra/AirTrafficController.git
##### 2. Once check out, perform the following maven command to build
    mvn clean install

## Class Diagram
![Class Diagram] (AirportTrafficController-Diagram.gif)

## Main Entities
##### 1. Gate : A model Object represent the Gate in the Airport
##### 2. Flight : A model Object represent the Flight
##### 3. AirportRegistry : Keep track of all the flights and the gate and their association information
##### 4. AdminController which can add/remove/move the gate under maintenance or vice versa
##### 4. AirTrafficController which assign the flight to next available gate based on the size and also remove the flight and make the gate available for next flight

## Enhancement
##### 1. Design of Main Router Class which can take input from the Systems or user and pass the request to either of the Controller accordingly
##### 2. Building of Webservice based on the API
 
 

