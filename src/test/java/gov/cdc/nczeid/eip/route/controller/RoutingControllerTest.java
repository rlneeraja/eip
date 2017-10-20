package gov.cdc.nczeid.eip.route.controller;

import gov.cdc.nczeid.eip.route.model.Route;
import gov.cdc.nczeid.eip.route.services.RoutingService;
import gov.cdc.nczeid.eip.uilt.LoadJson;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations="classpath:application-test.yml")
public class RoutingControllerTest {

    private static final String APPLICATION_JSON = "application/json";
    @Value("${serverRootURL}")
    private String serverURL ;

    private String routeEndpoint;

    @Autowired
    private RoutingService service;


    @Before
    public void setup() throws Exception {
        this.routeEndpoint = serverURL + "/routing-services/v1/";
    }

    @After
    public void teardown() throws Exception {
        //super.tearDown();


    }
  private void saveRoute(String fileName) {
        Route route = LoadJson.readJson(fileName);
        given()
                .contentType(APPLICATION_JSON)
                .body(route)
                .when()
                .post(this.routeEndpoint + "/route/routeMessage")
                .then()
                .statusCode(200);
    }
  
 

    @Test
    public void testSaveSimpleRoute() throws Exception {
    	saveRoute("simpleMessage.txt");
    }


    @Test
    public void testSave() throws Exception {
    	service.save(LoadJson.readJson("simpleMessage.txt"));
    }

    @Test
    public void testGetAll() throws Exception {
        service.getAll();
    }
    
    @Test
    public void testFindByRguid() throws Exception {
        service.findByRguid("");
    }

}