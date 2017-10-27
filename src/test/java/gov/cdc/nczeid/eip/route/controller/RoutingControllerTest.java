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
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

<<<<<<< HEAD
import static io.restassured.RestAssured.given;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
=======
import gov.cdc.nczeid.eip.route.model.Route;
import gov.cdc.nczeid.eip.route.services.RoutingService;
import gov.cdc.nczeid.eip.utils.LoadJson;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.junit.Assert.*;

import java.util.List;

@RunWith(SpringRunner.class)
>>>>>>> neeraja
@TestPropertySource(locations="classpath:application-test.yml")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT) 
public class RoutingControllerTest {

    private static final String APPLICATION_JSON = "application/json";
    @Value("${serverRootURL}")
    private String serverURL ;

    private String routeEndpoint;

<<<<<<< HEAD
    @Autowired
    private RoutingService service;


    @Before
    public void setup() throws Exception {
        this.routeEndpoint = serverURL + "/routing-services/v1/";
=======
    
    @Before
    public void setup() throws Exception {
        this.rootAPIIURL = serverURL + "/routing-services/v1/route";
        this.routeEndpoint = rootAPIIURL;
        System.out.println(routeEndpoint);
>>>>>>> neeraja
    }

    @After
    public void teardown() throws Exception {
        //super.tearDown();


    }
 // save good messsage
    @Test
  public void testSaveRoute() {
        Route route = LoadJson.readJson("createRoute.txt");
        given()
                .contentType(APPLICATION_JSON)
                .body(route)
                .when()
                .post(this.routeEndpoint + "/route/routeMessage")
                .then()
                .statusCode(200);
    }
  
 
    
// // save duplicate messsage
//    @Test
//    public void testSaveDuplicate() throws Exception {
//    	 Route route = LoadJson.readJson("createRoute.txt");
//         given()
//                 .contentType(APPLICATION_JSON)
//                 .body(route)
//                 .when()
//                 .post(this.routeEndpoint)
//                 .then()
//                 .statusCode(409);
//                // .statusCode(200);
//        
//    }
    // Tests for findAll
    @Test
    public void testgetAllRoutes() {
        Response response = when().get(this.rootAPIIURL + "/routes")
                .then()
                .statusCode(200)
                .extract().response();
         response.body().prettyPrint();
    }
    
//    // Tests for getby rguid
//    @Test
//    public void testRetrieveRouteByID() {
//        Response response = when().get(this.rootAPIIURL + "/25ff15cd-7d93-4d9d-b936-1107f38ed20d")
//                .then()
//                .statusCode(200)
//                .extract().response();
//         response.body().prettyPrint();
//    }
//    
    @Test
    public void testRetrieveRouteByIDNotFound() {
        Response response = when().get(this.rootAPIIURL + "/YYYYYYYYYS")
                .then()
                .statusCode(404)
                .extract().response();
         response.body().prettyPrint();
    }

}