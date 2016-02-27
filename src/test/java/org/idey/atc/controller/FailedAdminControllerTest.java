package org.idey.atc.controller;

import org.idey.atc.exception.AirportControllerException;
import org.idey.atc.model.Flight;
import org.idey.atc.model.Gate;
import org.idey.atc.model.constant.GateStatus;
import org.idey.atc.model.constant.Size;
import org.idey.atc.rule.ExceptionLoggingRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class FailedAdminControllerTest extends AbstractAdminControllerTest {
    private GateStatus status;
    private Flight f;
    private Size gateSize;

    public FailedAdminControllerTest(int gateId, GateStatus status,
                                     Size gateSize, Flight f) {
        super(gateId);
        this.status = status;
        this.gateSize=gateSize;
        this.f=f;
    }
    @Rule
    public ExceptionLoggingRule exceptionLoggingRule = new ExceptionLoggingRule();
    @Rule public ExpectedException expectedException = ExpectedException.none();


    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {-1,null,null,null},
                {1, GateStatus.OCCUPIED,Size.SMALL,new Flight(21,Size.SMALL)}
        });
    }

    @Test
    public void failedGateAdd() throws AirportControllerException {
        expectedException.expect(Exception.class);
        Gate g=null;
        if(gateId!=-1){
            g = new Gate(gateId,gateSize,status);
            g.setFlight(f);
        }
        AdminAirportController controller = new AdminAirportControllerImpl();
        controller.addGate(g);
    }

    @Test
    public void failedGateRemove() throws AirportControllerException {
        expectedException.expect(Exception.class);
        AdminAirportController controller = new AdminAirportControllerImpl();
        if(gateId!=-1){
            Gate g = new Gate(gateId,gateSize);
            controller.addGate(g);
            AirTrafficController airTrafficController = new AirTrafficControllerImpl();
            airTrafficController.assignedFlightGate(f.getFlightNumber(),f.getFlightSize());
        }
        controller.removeGate(gateId);
    }

    @Test
    public void failedGateMoveToMaintenanceAndViceVersa() throws AirportControllerException {
        expectedException.expect(Exception.class);
        AdminAirportController controller = new AdminAirportControllerImpl();
        if(gateId!=-1){
            Gate g = new Gate(gateId,gateSize);
            controller.addGate(g);
            AirTrafficController airTrafficController = new AirTrafficControllerImpl();
            airTrafficController.assignedFlightGate(f.getFlightNumber(), f.getFlightSize());
        }
        controller.moveGateUnderMaintenanceAndViceVersa(gateId, true);

    }



}
