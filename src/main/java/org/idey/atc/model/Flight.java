package org.idey.atc.model;


import org.idey.atc.model.constant.Size;

/**
 * @author indranil dey
 * Class representing Fligh Class
 */
public class Flight {
    private final int flightNumber;
    private final Size flightSize;

    public Flight(final int flightNumber, final Size flightSize) {
        this.flightNumber = flightNumber;
        this.flightSize = flightSize;
    }

    public int getFlightNumber() {
        return flightNumber;
    }

    public Size getFlightSize() {
        return flightSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Flight flight = (Flight) o;

        return flightNumber == flight.flightNumber;

    }

    @Override
    public int hashCode() {
        return flightNumber;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("{Flight Number:").append(flightNumber)
                .append(", Flight Size:").append(flightSize).append("}").toString();
    }
}
