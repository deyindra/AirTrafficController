package org.idey.atc.model.constant;

import java.util.EnumSet;
import java.util.Set;

/**
 * @author indranil dey
 * An enum respresent the the size of {@link org.idey.atc.model.Flight} and {@link org.idey.atc.model.Gate}
 */
public enum Size {
    /**
     * Small Gate size
     */
    SMALL(0),
    /**
     * Medium Gate size
     */
    MEDIUM(1),
    /**
     * Large Gate size
     */
    LARGE(2);

    /**
     * Index position if one place all the values in the sorted array
     */
    private int position;

    Size(int position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return name();
    }

    public int getPosition() {
        return position;
    }

}
