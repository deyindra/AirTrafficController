package org.idey.atc.controller;


import org.idey.atc.exception.AirportControllerException;
import org.idey.atc.model.Gate;
import org.idey.atc.model.constant.Size;

public interface AirTrafficController {
    // Assign a flight to the gate
    Gate assignedFlightGate(int flightId,Size flightSize) throws AirportControllerException;
    //De Assign a flight from a gate and make the available for another flight
    void removeFlightGate(int flightId) throws AirportControllerException;
}
