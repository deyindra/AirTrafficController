package org.idey.atc.controller;

import org.idey.atc.exception.AirportControllerException;
import org.idey.atc.model.Gate;

public interface AdminAirportController {
    //Add new gate to the airport
    void addGate(Gate gate) throws AirportControllerException;
    //Remove gate from the airport
    void removeGate(int gateId) throws AirportControllerException;
    //Move a available gate to maintenance mode and vice versa
    void moveGateUnderMaintenanceAndViceVersa(int gateId, boolean isUnderMaintenance) throws AirportControllerException;
    // Get a Gate information
    Gate getGateInfo(int id)throws AirportControllerException;
}
