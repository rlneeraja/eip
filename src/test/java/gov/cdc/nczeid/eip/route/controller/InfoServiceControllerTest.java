package gov.cdc.nczeid.eip.route.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import gov.cdc.nczeid.eip.route.About;
import io.restassured.response.Response;

import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.containsString;

/**
 * Created by Neeraja on 10/4/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT) 
@TestPropertySource(locations="classpath:application-test.yml")
public class InfoServiceControllerTest {

    @Autowired
    private About about;
    
    @Value("${serverRootURL}")
    private String serverURL;

    private String rootAPIIURL;
    private String routeEndpoint;

    @Before
    public void setUp() throws Exception {
    	  this.rootAPIIURL = serverURL + "/routing-services/v1/";
          this.routeEndpoint = rootAPIIURL;

    }

    @Test
    public void about() throws Exception {
        Response response = when().get(this.rootAPIIURL  + "info/about")
                .then()
                .statusCode(200)
                .extract().response();
         response.body().prettyPrint();
    }

    @Test
    public void printAbout() throws Exception {
        System.out.println("about = " + about);
    }

}