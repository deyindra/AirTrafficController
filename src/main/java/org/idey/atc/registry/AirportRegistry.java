package org.idey.atc.registry;

import org.idey.atc.model.Flight;
import org.idey.atc.model.Gate;
import org.idey.atc.model.constant.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author indranildey
 * A Registry class to keep track of Flight and Gate information with the no of Available and Non Available Gates
 *
 */
public class AirportRegistry {
    private static volatile AirportRegistry INSTANCE =null;
    private static final Logger LOGGER = LoggerFactory.getLogger(AirportRegistry.class);
    //Flight Registry which maintain how many fights are at airport at a give time
    private ConcurrentMap<Integer, Flight> flightRegistry;
    //Gate Registry which maintain how many gates are in the airport
    private ConcurrentMap<Integer, Gate> gateRegistry;
    //Flight gate registry maintain which flight currently at what gate
    private ConcurrentMap<Flight, Gate> flightGateRegistry;
    //No of Available gate per size
    private ConcurrentMap<Size, Set<Gate>> availableGatesParSize;
    /**
     *     No of Non Available gate per size with status {@link org.idey.atc.model.constant.GateStatus#OCCUPIED}
     *     or {@link org.idey.atc.model.constant.GateStatus#UNDER_MAINTENANCE}
     */
    private ConcurrentMap<Size, Set<Gate>> notAvailableGatesParSize;
    private final Lock readLock;
    private final Lock writeLock;

    public static AirportRegistry getInstance(){
        if(INSTANCE==null){
            synchronized (AirportRegistry.class){
                if(INSTANCE==null){
                    INSTANCE = new AirportRegistry();
                }
            }
        }
        return INSTANCE;
    }

    private AirportRegistry() {
        flightRegistry = new ConcurrentHashMap<>();
        gateRegistry = new ConcurrentHashMap<>();
        flightGateRegistry = new ConcurrentHashMap<>();
        availableGatesParSize = new ConcurrentHashMap<>();
        notAvailableGatesParSize = new ConcurrentHashMap<>();
        ReentrantReadWriteLock rw = new ReentrantReadWriteLock();
        readLock=rw.readLock();
        writeLock=rw.writeLock();
    }


    public Lock getReadLock() {
        return readLock;
    }

    public Lock getWriteLock() {
        return writeLock;
    }

    /**
     * Add gate to the registry
     * @param id Id of {@link Gate}
     * @param gate {@link Gate}
     * @return boolean
     */
    public boolean addGateToRegistry(int id, Gate gate){
        writeLock.lock();
        try {
            return (gateRegistry.putIfAbsent(id, gate)) == null;
        }finally {
            writeLock.unlock();
        }
    }

    /**
     * Add gate to the registry
     * @param id Id of {@link Gate}
     * @return boolean return true for success
     */
    public boolean removeGateFromRegistry(int id){
        writeLock.lock();
        try {
            return gateRegistry.remove(id) != null;
        }finally {
            writeLock.unlock();
        }
    }

    /**
     * Add flight to the registry
     * @param id id of the flight
     * @param flight {@link Flight}
     * @return boolean
     */
    public boolean addFlightToRegistry(int id, Flight flight){
        writeLock.lock();
        try {
            return (flightRegistry.putIfAbsent(id, flight)) == null;
        }finally {
            writeLock.unlock();
        }
    }

    /**
     * Remove flight from registry
     * @param id id of the {@link Flight}
     * @return {@link Flight}
     */
    public Flight removeFlightFromRegistry(int id){
        writeLock.lock();
        try {
            return flightRegistry.remove(id);
        }finally {
            writeLock.unlock();
        }
    }

    /**
     * Assign the flight to the gate
     * @param f {@link Flight}
     * @param g {@link Gate}
     * @return true if success
     */
    public boolean assignFlightToGate(Flight f, Gate g){
        writeLock.lock();
        try {
            return (flightGateRegistry.putIfAbsent(f, g)) == null;
        }finally {
            writeLock.unlock();
        }
    }

    /**
     * De assign the flight from the gate
     * @param f {@link Flight}
     * @return {@link Gate}
     */
    public Gate removeFlightFromGate(Flight f){
        writeLock.lock();
        try {
            return flightGateRegistry.remove(f);
        }finally {
            writeLock.unlock();
        }
    }

    /**
     * Get the Get information from the flight
     * @param f {@link Flight}
     * @return {@link Gate}
     */
    public Gate getGateInfoFromFlight(Flight f){
        readLock.lock();
        try {
            return flightGateRegistry.get(f);
        }finally {
            readLock.unlock();
        }
    }

    /**
     * Assign the gate to Available or Non Available list
     * @param gate {@link Gate}
     * @param isAvailable true if one need to add to the available gates
     * @return true if success
     */
    public boolean assignGateToList(Gate gate, boolean isAvailable){
        writeLock.lock();
        try {
            ConcurrentMap<Size, Set<Gate>> currentMap = isAvailable ?
                    availableGatesParSize : notAvailableGatesParSize;
            Size s = gate.getGateSize();
            Set<Gate> sets = currentMap.getOrDefault(s, new LinkedHashSet<>());
            boolean status = sets.add(gate);
            currentMap.put(s, sets);
            LOGGER.info("Current Available gates per size {}", currentMap);
            return status;
        }finally {
            writeLock.unlock();
        }
    }

    /**
     * De assign the gate to Available or Non Available list
     * @param gate {@link Gate}
     * @param isAvailable true if one need to de assign to the available gates
     * @return true if success
     */
    public boolean removeGateFromList(Gate gate, boolean isAvailable){
        writeLock.lock();
        try {
            ConcurrentMap<Size, Set<Gate>> currentMap = isAvailable ?
                    availableGatesParSize : notAvailableGatesParSize;
            Size s = gate.getGateSize();
            Set<Gate> sets = currentMap.get(s);
            if (sets != null && !sets.isEmpty()) {
                return sets.remove(gate);
            }
            LOGGER.info("Current Available gates per size {}", currentMap);
            return false;
        }finally {
            writeLock.unlock();
        }
    }

    public Gate getGateInfo(int id){
        readLock.lock();
        try {
            return gateRegistry.getOrDefault(id, null);
        }finally {
            readLock.unlock();
        }
    }



    public Set<Gate> getAvailableGates(Size size){
        readLock.lock();
        try {
            return availableGatesParSize.getOrDefault(size, null);
        }finally {
            readLock.unlock();
        }
    }

    public Set<Gate> getNonAvailableGates(Size size){
        readLock.lock();
        try {
            return notAvailableGatesParSize.getOrDefault(size, null);
        }finally {
            readLock.unlock();
        }
    }

}
