package org.idey.atc.model.constant;

/**
 * @author indranil dey
 * Define the status of the gate
 */
public enum  GateStatus {
    /**
     * Gate is Available not flight is present at the gate
     */
    AVAILABLE,
    /**
     * Flight is present at the gate
     */
    OCCUPIED,

    /**
     * Gate is under maintenance
     */
    UNDER_MAINTENANCE;

    @Override
    public String toString() {
        return name();
    }
}
