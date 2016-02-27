package org.idey.atc.controller;

import org.idey.atc.exception.AirportControllerException;
import org.idey.atc.model.Gate;
import org.idey.atc.model.constant.Size;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MultiRequestAirTrafficControllerTest {
    private AdminAirportController adminAirportController;
    private AirTrafficController airTrafficController;
    private Size[] sizes;
    @Before
    public void setup(){
        adminAirportController = new AdminAirportControllerImpl();
        airTrafficController = new AirTrafficControllerImpl();
        sizes = new Size[]{Size.SMALL, Size.MEDIUM, Size.LARGE};
    }



    @Test
    public void testConcurrency() throws InterruptedException, AirportControllerException {
        int numberOfGates = 9;
        int numberOfFlight = 6;
        Thread[] adminThreads = new Thread[numberOfGates];
        for(int i=0;i<numberOfGates;i++){
            final int index = i;
            adminThreads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        adminAirportController.addGate(new Gate(index+1,sizes[index%sizes.length]));
                    } catch (AirportControllerException e) {
                        throw new IllegalArgumentException(e);
                    }
                }
            });
        }
        for(Thread t: adminThreads){
            t.start();
            t.join();
        }
        Thread[] flightThreads = new Thread[numberOfFlight];
        for(int i=0;i<numberOfFlight;i++){
            final int index = i;
            flightThreads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        airTrafficController.assignedFlightGate(index+1, Size.MEDIUM);
                    } catch (AirportControllerException e) {
                        throw new IllegalArgumentException(e);
                    }
                }
            });
        }
        for(Thread t: flightThreads){
            t.start();
            t.join();
        }
        Assert.assertNotNull(adminAirportController.getGateInfo(6).getFlight());


    }
}
