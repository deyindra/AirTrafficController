package org.idey.atc.controller;

import org.idey.atc.registry.AirportRegistry;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

public abstract class AbstractControllerTest {
    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractControllerTest.class);
    @Before
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        Field instance = AirportRegistry.class.getDeclaredField("INSTANCE");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @After
    public void cleanup() throws NoSuchFieldException, IllegalAccessException {
        Field instance = AirportRegistry.class.getDeclaredField("INSTANCE");
        instance.setAccessible(true);
        instance.set(null, null);
    }
}
