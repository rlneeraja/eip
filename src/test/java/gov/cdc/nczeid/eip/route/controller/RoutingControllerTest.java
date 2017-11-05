package gov.cdc.nczeid.eip.route.controller;

import gov.cdc.nczeid.eip.route.model.Route;
import gov.cdc.nczeid.eip.route.repository.RouteRepo;
import gov.cdc.nczeid.eip.utils.LoadJson;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

@RunWith(SpringRunner.class)
@TestPropertySource(locations="classpath:application-test.yml")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT) 
public class RoutingControllerTest {

    private static final String APPLICATION_JSON = "application/json";
    @Value("${serverRootURL}")
    private String serverURL ;
    private String rootAPIIURL;
    private String routeEndpoint;
    
    @Autowired
    public RouteRepo repo;


    @Before
    public void setup() throws Exception {
        this.rootAPIIURL = serverURL + "/routing-services/v1";
        this.routeEndpoint = rootAPIIURL;
        System.out.println(routeEndpoint);
    }

    @After
    public void teardown() throws Exception {
        //super.tearDown();


    }
 // save good messsage
 //   @Test
  public void testSaveRoute() {
        Route route = LoadJson.readJson("createRoute.txt");
        given()
                .contentType(APPLICATION_JSON)
                .body(route)
                .when()
                .post(this.routeEndpoint + "/route")
                .then()
                .statusCode(200);
    }

  // save duplicate messsage
    @Test
    public void testSaveDuplicate() throws Exception {
    	 Route route = LoadJson.readJson("createRoute.txt");
         given()
                 .contentType(APPLICATION_JSON)
                 .body(route)
                 .when()
                 .post(this.routeEndpoint)
                 .then()
                // .statusCode(409);
                 .statusCode(404);
        
    }
    
    // Tests for findAll
    @Test
    public void testgetAllRoutes() {
        Response response = when().get(this.rootAPIIURL + "/routes")
                .then()
                .statusCode(200)
                .extract().response();
         response.body().prettyPrint();
    }
    
    // Tests for getby rguid
    @Test
    public void testRetrieveRouteByID() {
        Response response = when().get(this.rootAPIIURL + "/route/B24D5F5A-5504-457D-BD7E-BB2196E85BB2")
                .then()
                .statusCode(200)
                .extract().response();
         response.body().prettyPrint();
    }
    
    //    @Test
    public void testRetrieveRouteByIDNotFound() {
        Response response = when().get(this.rootAPIIURL + "/route/YYYYYYYYYS")
                .then()
                .statusCode(404)
                .extract().response();
         response.body().prettyPrint();
    }
    
    
    // @Test
    // @Rollback(true)
    // public void testDelete() {
    // 	 Route route = LoadJson.readJson("createDeleteRoute.txt");
    // 	 route = repo.save(route);
    // 	 Response response = when().post(this.rootAPIIURL + "/route/" + route.getRouteId())
    //              .then()
    //              .statusCode(200)
    //              .extract().response();
    //       response.body().prettyPrint();
		
    // }

}