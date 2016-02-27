package org.idey.atc.controller;

import org.idey.atc.exception.AirportControllerException;
import org.idey.atc.model.Flight;
import org.idey.atc.model.Gate;
import org.idey.atc.model.constant.GateStatus;
import org.idey.atc.model.constant.Size;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class SuccessAirTrafficControllerTest extends AbstractAirTrafficControllerTest {

    public SuccessAirTrafficControllerTest(Flight[] flights,
                                           Gate[] gates) {
        super(flights, gates);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new Flight[]{new Flight(1,Size.MEDIUM)},
                        new Gate[]{new Gate(1, Size.SMALL),
                        new Gate(2, Size.SMALL),
                        new Gate(3, Size.LARGE)}
                },
                {new Flight[]{new Flight(1,Size.MEDIUM)
                            ,new Flight(1,Size.MEDIUM)},
                        new Gate[]{new Gate(1, Size.SMALL),
                                new Gate(2, Size.SMALL),
                                new Gate(3, Size.LARGE)}
                },
                {new Flight[]{new Flight(1,Size.SMALL)},
                        new Gate[]{new Gate(1, Size.SMALL),
                                new Gate(2, Size.SMALL),
                                new Gate(3, Size.LARGE)}
                },

        });
    }

    @Test
    public void successAddAndRemove() throws AirportControllerException {
        AdminAirportController adminAirportController = new AdminAirportControllerImpl();
        AirTrafficController airTrafficController = new AirTrafficControllerImpl();
        for(Gate g:this.gates){
            adminAirportController.addGate(g);
        }
        for(Flight f:this.flights){
            Gate g = airTrafficController.assignedFlightGate(f.getFlightNumber(),f.getFlightSize());
            Assert.assertNotNull(g.getFlight());
            airTrafficController.removeFlightGate(f.getFlightNumber());
            Assert.assertNull(g.getFlight());
        }
    }



}
