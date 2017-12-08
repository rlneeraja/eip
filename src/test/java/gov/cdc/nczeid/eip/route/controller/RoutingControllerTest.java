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
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

import java.sql.DriverManager;

@RunWith(SpringRunner.class)
@TestPropertySource(locations="classpath:application-test.yml")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT) 
public class RoutingControllerTest {

    private static final String APPLICATION_JSON = "application/json";
   
    @Autowired
    public RouteRepo repo;

    
    @Value("${server.apiContext}")
    private String apiContext;
    
    @Value("${server.context-path}")
    private String contextPath;

    
    @Value("${server.versionContext}")
    private String versionContext;
    
    @Value("${server.port}")
    private String port;
    
    private EmbeddedDatabase db;
    
    private String rootAPIIURL;

    @Before
    public void setUp() throws Exception {
        this.rootAPIIURL = "http://localhost:10001"+
        		contextPath + apiContext+ "/"+  versionContext+ "1";
    }
  
   

    @After
    public void teardown() throws Exception {

    }
    
 // save good messsage
  @Test
  public void testCreateRoute() {
        Route route = LoadJson.readJson("createRoute.txt");
        given()
                .contentType(APPLICATION_JSON)
                .body(route)
                .when()
                .post(this.rootAPIIURL + "/route")
                .then()
                .statusCode(201);
        route = repo.findByConditionAndDestination(route.getCondition(), route.getDestination()).iterator().next();
        repo.delete(route);
    }

  // save duplicate messsage
    @Test
    public void testCreateDuplicate() throws Exception {
    	 Route route = LoadJson.readJson("createRoute.txt");
         given()
                 .contentType(APPLICATION_JSON)
                 .body(route)
                 .when()
                 .post(this.rootAPIIURL + "/route")
                 .then()
                 .statusCode(201);
    	 route = LoadJson.readJson("createRoute.txt");
         given()
                 .contentType(APPLICATION_JSON)
                 .body(route)
                 .when()
                 .post(this.rootAPIIURL + "/route")
                 .then()
                // .statusCode(409);
                 .statusCode(400);
         route = repo.findByConditionAndDestination(route.getCondition(), route.getDestination()).iterator().next();
         repo.delete(route);
    }
    
    @Test
    public void testUpdateRoute() {
    	 Route route = LoadJson.readJson("deleteRoute.txt");
         route = repo.save(route);
         route = repo.findByRouteId(route.getRouteId());
         route.setCondition("222");
         given()
                  .contentType(APPLICATION_JSON)
                  .body(route)
                  .when()
                  .put(this.rootAPIIURL + "/route")
                  .then()
                  .statusCode(200);
         route = repo.findByRouteId(route.getRouteId());
         repo.delete(route);
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
        Response response = when().get(this.rootAPIIURL + "/route/bde3140d-8fdf-4034-b683-00c782648ded")
                .then()
                .statusCode(200)
                .extract().response();
         response.body().prettyPrint();
    }
    
    
    @Test
    public void testRetrieveRouteByIDNotFound() {
        Response response = when().get(this.rootAPIIURL + "/route/YYYYYYYYYS")
                .then()
                .statusCode(400)
                .extract().response();
         response.body().prettyPrint();
    }
    
    
    @Test
    @Rollback(true)
    public void testDelete() {
    	 Route route = LoadJson.readJson("deleteRoute.txt");
         route = repo.save(route);
         route = repo.findByRouteId(route.getRouteId());
          Response responseDel = when().put(this.rootAPIIURL + "/route/" + route.getRouteId() + "," + route.getVersion() )
                  .then()
                  .statusCode(200)
                  .extract().response();
          route = repo.findByRouteId(route.getRouteId());
          repo.delete(route);
    }
    
    // Tests routing
    @Test
    public void testRouteMessage() {
        Response response = when().post(this.rootAPIIURL + "/routeMessage?condition=11545&mguid=bde3140d-8fdf-4034-b683-00c782648ded")
                .then()
                .statusCode(200)
                .extract().response();
         response.body().prettyPrint();
    }

}