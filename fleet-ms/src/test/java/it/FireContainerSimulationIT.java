package it;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.gson.Gson;

import ibm.labs.kc.dto.model.ShipSimulationControl;

/**
 * Start moving ship and simulate a fire on container.
 * This is an integration test so we need access to Kafka brokers
 * @author jerome boyer
 *
 */
public class FireContainerSimulationIT extends BaseIntegrationTest {

	private String endpoint = "/ships/simulate";
    private String url = baseUrl + endpoint;

    @Test
    public void testFireFourContainers() throws Exception {
        System.out.println("Testing endpoint " + url);
        ShipSimulationControl ctl = new ShipSimulationControl("JimminyCricket", ShipSimulationControl.CONTAINER_FIRE);
        int maxCount = 5;
        int responseCode = makePostRequest(url,new Gson().toJson(ctl));
        for(int i = 0; (responseCode != 200) && (i < maxCount); i++) {
          System.out.println("Response code : " + responseCode + ", retrying ... (" + i + " of " + maxCount + ")");
          Thread.sleep(5000);
          responseCode = makeGetRequest(url);
        }
        assertTrue("Incorrect response code: " + responseCode, responseCode == 200);
    }


}
