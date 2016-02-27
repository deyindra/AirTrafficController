package org.idey.atc.controller;

import org.idey.atc.model.Flight;
import org.idey.atc.model.Gate;

public class AbstractAirTrafficControllerTest extends AbstractControllerTest{
    protected Gate[] gates;
    protected Flight[] flights;

    public AbstractAirTrafficControllerTest(Flight[] flights, Gate[] gates) {
        super();
        this.flights = flights;
        this.gates = gates;
    }
}
