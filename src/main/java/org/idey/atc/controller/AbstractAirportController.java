package org.idey.atc.controller;

import org.idey.atc.registry.AirportRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract Airport Controller class
 * @see AdminAirportControllerImpl
 * @see AirTrafficControllerImpl
 *
 */
public abstract class AbstractAirportController {
    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractAirportController.class);
    /**
     * {@link AirportRegistry} singleton object which will hold all the gate and Flight information
      */
    protected AirportRegistry registry;
    protected AbstractAirportController() {
        registry = AirportRegistry.getInstance();
    }
}
