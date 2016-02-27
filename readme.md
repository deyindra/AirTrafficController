# Air Traffic Controller System
## Overview
A OOPS design for ATC (Air Traffic Controller) which control the flights coming to and going from Airport and assign them to the available gate based on there size.

 
## Scope
Scope is limited to design two Main controller class which will manage main two entities in the System, which are Gate and Flight respectively.
  
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
##### 1. Get the code from github 