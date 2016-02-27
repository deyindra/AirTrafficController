package org.idey.atc.controller;

import org.idey.atc.exception.AirportControllerException;
import org.idey.atc.model.Gate;
import org.idey.atc.model.constant.GateStatus;
import org.idey.atc.model.constant.Size;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class SuccessAdminControllerTest extends AbstractAdminControllerTest {
    private GateStatus status;

    public SuccessAdminControllerTest(int gateId, GateStatus status) {
        super(gateId);
        this.status = status;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {1,null},
                {2, GateStatus.UNDER_MAINTENANCE}
        });
    }

    @Test
    public void successGateAdd() throws AirportControllerException {
        Gate g;
        if(status==null){
            g = new Gate(gateId, Size.SMALL);
        }else{
            g = new Gate(gateId, Size.SMALL, status);
        }
        AdminAirportController controller = new AdminAirportControllerImpl();
        controller.addGate(g);
        Assert.assertEquals(g, controller.getGateInfo(gateId));
    }

    @Test
    public void successGateRemove() throws AirportControllerException {
        Gate g;
        if(status==null){
            g = new Gate(gateId, Size.SMALL);
        }else{
            g = new Gate(gateId, Size.SMALL, status);
        }
        AdminAirportController controller = new AdminAirportControllerImpl();
        controller.addGate(g);
        controller.removeGate(gateId);
        Assert.assertNull(controller.getGateInfo(gateId));
    }


    @Test
    public void successGateMovedToMaintenanceAndViceVersa() throws AirportControllerException {
        Gate g;
        if(status==null){
            g = new Gate(gateId, Size.SMALL);
        }else{
            g = new Gate(gateId, Size.SMALL, status);
        }
        AdminAirportController controller = new AdminAirportControllerImpl();
        controller.addGate(g);
        GateStatus targetStatus = g.getStatus()==GateStatus.AVAILABLE ? GateStatus.UNDER_MAINTENANCE : GateStatus.AVAILABLE;
        boolean isUnderMaintenance = g.getStatus()==GateStatus.AVAILABLE;
        controller.moveGateUnderMaintenanceAndViceVersa(gateId,isUnderMaintenance);
        Assert.assertEquals(targetStatus, g.getStatus());
    }


}
