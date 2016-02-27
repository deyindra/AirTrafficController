package org.idey.atc.controller;

public abstract class AbstractAdminControllerTest extends AbstractControllerTest{
    protected int gateId;

    protected AbstractAdminControllerTest(int gateId) {
        this.gateId = gateId;
    }
}
