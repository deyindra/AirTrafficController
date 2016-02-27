package org.idey.atc.controller;

import org.idey.atc.exception.AirportControllerException;
import org.idey.atc.model.Gate;
import org.idey.atc.model.constant.Size;
import org.idey.atc.rule.ExceptionLoggingRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class FailedAirTrafficControllerTest {
    @Rule
    public ExceptionLoggingRule exceptionLoggingRule = new ExceptionLoggingRule();
    @Rule public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void failedAssignFlightAndRemoveTest() throws AirportControllerException {
        expectedException.expect(Exception.class);
        AdminAirportController controller = new AdminAirportControllerImpl();
        controller.addGate(new Gate(1, Size.SMALL));
        controller.addGate(new Gate(2, Size.SMALL));
        controller.addGate(new Gate(3, Size.MEDIUM));

        AirTrafficController airTrafficController = new AirTrafficControllerImpl();
        airTrafficController.assignedFlightGate(1,Size.SMALL);
        airTrafficController.assignedFlightGate(2,Size.SMALL);
        airTrafficController.assignedFlightGate(3,Size.SMALL);
        airTrafficController.assignedFlightGate(4,Size.SMALL);

    }



}
