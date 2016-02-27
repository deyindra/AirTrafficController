package org.idey.atc.controller;

import org.idey.atc.exception.AirportControllerException;
import org.idey.atc.model.Flight;
import org.idey.atc.model.Gate;
import org.idey.atc.model.constant.GateStatus;
import org.idey.atc.model.constant.Size;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;

/**
 * @author indranildey
 * Concrete class respresent the logic for AirportTrafficController
 */
public class AirTrafficControllerImpl
        extends AbstractAirportController implements AirTrafficController{
    /**
     * {@link Size} array which sorted and help to find the Next Available Gate based on the given size
     */
    private Size[] sizes;


    public AirTrafficControllerImpl() {
        super();
        this.sizes = Size.values();
        // Sort the Size array based on the positional index
        Arrays.sort(sizes, new Comparator<Size>() {
            @Override
            public int compare(Size o1, Size o2) {
                return o1.getPosition()-o2.getPosition();
            }
        });
    }

    /**
     * Return the {@link Gate} assigned to the flight
     * @param flightId flight ID of the Flight
     * @param flightSize {@link Size} of the flight
     * @return Return {@link Gate} assigned to the fligh
     * @throws AirportControllerException if no gates are available or flightSize supplied as null
     */
    @Override
    public Gate assignedFlightGate(int flightId,Size flightSize) throws AirportControllerException {
        registry.getWriteLock().lock();
        try {
            Gate returnGate;
            //throw exception if flight size is null
            if (flightSize == null) {
                throw new AirportControllerException("Flight size can not be null");
            }else{
                //create the fight object
                Flight f = new Flight(flightId,flightSize);
                // Check if the flight is already assigned to the gate
                returnGate = registry.getGateInfoFromFlight(f);
                // if it is null then find  nextAvailable gate
                if(returnGate==null) {
                    returnGate = getNextAvailableGate(flightSize);
                    // if no gate is found throw Error
                    if (returnGate == null) {
                        throw new AirportControllerException(String.format("No more gates are " +
                                "available for flight %d", flightId));
                    } else {
                        //Assign the gate to the flight and update registry accordingly
                        registry.removeGateFromList(returnGate, true);
                        returnGate.setStatus(GateStatus.OCCUPIED);
                        returnGate.setFlight(f);
                        registry.assignGateToList(returnGate, false);
                        registry.assignFlightToGate(f, returnGate);
                        registry.addFlightToRegistry(flightId, f);
                    }
                }else{
                    LOGGER.info("Flight {} is already assigned to gate {} ", returnGate.getFlight().toString(), returnGate.toString());
                }
            }
            return returnGate;
        }finally {
            registry.getWriteLock().unlock();
        }
    }

    /**
     *
     * @param flightId flight ID of the flight which needs to be removed
     * @throws AirportControllerException if there is no matching flight with given flightID
     */
    @Override
    public void removeFlightGate(int flightId) throws AirportControllerException {
        registry.getWriteLock().lock();
        try{
            Flight f = registry.removeFlightFromRegistry(flightId);
            // Check if the Flight object is null or not
            if(f!=null){
                // Remove the fight from the gate
                Gate g = registry.removeFlightFromGate(f);
                // Move the gate from Non Available to Available list
                registry.removeGateFromList(g,false);
                g.setStatus(GateStatus.AVAILABLE);
                g.setFlight(null);
                registry.assignGateToList(g,true);
            }else{
                throw new AirportControllerException(String.format("Invalid Flight ID %d",flightId));
            }
        }finally {
            registry.getWriteLock().unlock();
        }
    }

    /**
     * This method return next available gate based on the flight size with binary search over
     * {@link AirTrafficControllerImpl#sizes}, total time complexity would be O(log(N))
     * @param s Given Size of the flight
     * @return {@link Gate} next available gate
     */
    private synchronized Gate getNextAvailableGate(final Size s){
        Gate returnGate = null;
        //Given a size check no of Available Gate
        Set<Gate> gates = registry.getAvailableGates(s);
        //if the list is null then search all the available gates from Next bigger Size onwards
        if(gates==null || gates.isEmpty()){
            //Low index will be index of the next Bigger Size
            int lowIndex = s.getPosition()+1;
            int highIndex = sizes.length-1;
            while(lowIndex<=highIndex){
                int mid = lowIndex+(highIndex-lowIndex)/2;
                Size midSize = sizes[mid];
                // Get the Available gates for Next Big Size
                Set<Gate> availableGates = registry.getAvailableGates(midSize);
                if(availableGates!=null && !availableGates.isEmpty()){
                    returnGate = availableGates.iterator().next();
                    highIndex =mid-1;
                }else{
                    lowIndex=mid+1;
                }
            }
        }else{
            returnGate = gates.iterator().next();
        }
        return returnGate;
    }
}
