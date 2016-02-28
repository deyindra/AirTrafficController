package org.idey.atc.controller;

import org.idey.atc.exception.AirportControllerException;
import org.idey.atc.model.Gate;
import org.idey.atc.model.constant.Size;
import org.idey.atc.registry.AirportRegistry;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

public class MultiRequestAirTrafficControllerTest {
    private AdminAirportController adminAirportController;
    private AirTrafficController airTrafficController;
    private Size[] sizes;
    @Before
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        Field instance = AirportRegistry.class.getDeclaredField("INSTANCE");
        instance.setAccessible(true);
        instance.set(null, null);
        adminAirportController = new AdminAirportControllerImpl();
        airTrafficController = new AirTrafficControllerImpl();
        sizes = new Size[]{Size.SMALL, Size.MEDIUM, Size.LARGE};
    }

    @After
    public void cleanup() throws NoSuchFieldException, IllegalAccessException {
        Field instance = AirportRegistry.class.getDeclaredField("INSTANCE");
        instance.setAccessible(true);
        instance.set(null, null);
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
        Assert.assertEquals(Size.MEDIUM,adminAirportController.getGateInfo(6).getFlight().getFlightSize());
        Assert.assertEquals(Size.LARGE, adminAirportController.getGateInfo(6).getGateSize());


    }
}
