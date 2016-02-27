package org.idey.atc.controller;

import org.idey.atc.exception.AirportControllerException;
import org.idey.atc.model.Flight;
import org.idey.atc.model.Gate;
import org.idey.atc.model.constant.GateStatus;

/**
 * @author indranil dey
 * This class is responsible for addiing gate, removing gate, move the gate under maintenance and vice versa and
 * return the information about specific gate
 *
 */
public class AdminAirportControllerImpl extends AbstractAirportController implements AdminAirportController{

    /**
     * Add gate to the registry only can add only Gate with {@link GateStatus#AVAILABLE}
     * or {@link GateStatus#UNDER_MAINTENANCE}
     * @param gate {@link Gate} object
     * @throws AirportControllerException
     */
    @Override
    public void addGate(Gate gate) throws AirportControllerException {
        registry.getWriteLock().lock();
        try{
            //check if the gate object is null
            if(gate==null){
                throw new AirportControllerException("Gate object is null");
            }else{
                int gateId = gate.getGateId();
                boolean status = registry.addGateToRegistry(gateId, gate);
                // check if the gate is already added
                if(!status){
                    throw new AirportControllerException(String.format("Gate %s is already" +
                            " exists in the registry", gate.toString()));
                }else{
                    // if the gate is with AVAILABLE status then add to the available registry,
                    // if the gate is with MAINTENANCE status then add to the non available registry
                    // else throw exception
                    if(gate.getStatus()==GateStatus.AVAILABLE) {
                        registry.assignGateToList(gate, true);
                    }else if(gate.getStatus()==GateStatus.UNDER_MAINTENANCE){
                        registry.assignGateToList(gate, false);
                    }else{
                        throw new AirportControllerException(String.format("Gate with gate status %s " +
                                "can not be add to the registry",gate.getStatus()));
                    }
                }
            }
        }finally {
            registry.getWriteLock().unlock();
        }
    }

    /**
     * Remove the gate from the registry only Gate with {@link GateStatus#AVAILABLE} or {@link GateStatus#UNDER_MAINTENANCE}
     * @param gateId gate id can be remove else it will throw {@link AirportControllerException}
     * @throws AirportControllerException
     */
    @Override
    public void removeGate(int gateId) throws AirportControllerException {
        registry.getWriteLock().lock();
        try{
            Gate g = registry.getGateInfo(gateId);
            if(g==null){
                throw new AirportControllerException(String.format("Invalid Gate number %d", gateId));
            }else{
                Flight f = g.getFlight();
                //this means there is no flight assigned to the gate
                if(f==null){
                    registry.removeGateFromRegistry(gateId);
                    GateStatus status = g.getStatus();
                    if(status==GateStatus.AVAILABLE){
                        registry.removeGateFromList(g,true);
                    }else{
                        registry.removeGateFromList(g,false);
                    }
                }else{
                    //Admin can not remove the gate if the flight is assigned to the gate
                    throw new AirportControllerException(String.format("Gate {%s} can not be removed " +
                            "as Flight {%s} assigned to the gate", g.toString(), f.toString()));
                }
            }
        }finally {
            registry.getWriteLock().unlock();
        }
    }

    /**
     *
     * @param gateId gateid
     * @param isUnderMaintenance flag to denote if one need to move the gate with {@link GateStatus#AVAILABLE}
     * to {@link GateStatus#UNDER_MAINTENANCE} and vice versa
     * @throws AirportControllerException
     */
    @Override
    public void moveGateUnderMaintenanceAndViceVersa(int gateId,
                                                     boolean isUnderMaintenance) throws AirportControllerException {
        registry.getWriteLock().lock();
        try{
            Gate g = registry.getGateInfo(gateId);
            // Check if the gate exists or not
            if(g==null){
                throw new AirportControllerException(String.format("Invalid Gate number %d", gateId));
            }else{
                Flight f = g.getFlight();
                // Check if the there is no flight associated with the gate
                if(f==null){
                    GateStatus status;
                    if(isUnderMaintenance) {
                        status=GateStatus.UNDER_MAINTENANCE;
                        LOGGER.info("Moved Gate {} under maintenance status ", g.toString());
                    }else{
                        status=GateStatus.AVAILABLE;
                        LOGGER.info("Moved Gate {} under available status ", g.toString());
                    }
                    registry.removeGateFromList(g, isUnderMaintenance);
                    g.setStatus(status);
                    registry.assignGateToList(g, !isUnderMaintenance);
                }else{
                    throw new AirportControllerException(String.format("Gate %s can not be moved under maintenance" +
                            "as Flight %s assigned to the gate", g.toString(), f.toString()));
                }
            }
        }finally {
            registry.getWriteLock().unlock();
        }
    }

    /**
     *
     * @param gateId Gate Id
     * @return {@link Gate} associated with the gateId
     */
    @Override
    public Gate getGateInfo(int gateId){
        registry.getReadLock().lock();
        try{
            return registry.getGateInfo(gateId);
        }finally {
            registry.getReadLock().unlock();
        }
    }
}
