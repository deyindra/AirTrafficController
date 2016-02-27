package org.idey.atc.model;


import org.idey.atc.model.constant.GateStatus;
import org.idey.atc.model.constant.Size;

/**
 * @author indranildey
 * Gate object contains number of the gate, {@link Flight} which is at gate, and {@link Size} of the gate
 *
 */

public class Gate {
    private final int gateId;
    private final Size gateSize;
    private Flight flight;
    private GateStatus status;

    public Gate(int gateId, Size gateSize, GateStatus status) {
        this.gateId = gateId;
        this.gateSize = gateSize;
        this.status = status;
    }

    public Gate(final int gateId, final Size gateSize) {
        this(gateId,gateSize, GateStatus.AVAILABLE);
    }


    public int getGateId() {
        return gateId;
    }

    public Size getGateSize() {
        return gateSize;
    }

    public Flight getFlight() {
        return flight;
    }

    public GateStatus getStatus() {
        return status;
    }


    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public void setStatus(GateStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Gate gate = (Gate) o;

        return gateId == gate.gateId;

    }

    @Override
    public int hashCode() {
        return gateId;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("{Gate ID:").append(gateId)
                .append(", Gate Size:").append(gateSize)
                .append(", Gate Status:").append(status).append("}").toString();
    }
}
