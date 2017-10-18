package gov.cdc.nczeid.eip.route.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import gov.cdc.nczeid.eip.route.About;

import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.containsString;

/**
 * Created by Neeraja on 10/4/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations="classpath:application.yml")
public class InfoServiceControllerTest {

    @Autowired
    private About about;

    private String rootAPIIURL;

    @Before
    public void setUp() throws Exception {
        this.rootAPIIURL = "/v1/";

    }

    @Test
    public void about() throws Exception {
        when().
                get(this.rootAPIIURL + "info/about").
        then().
                statusCode(200).
                body("summary", containsString("REST Services to receive EIP messages"));
    }

    @Test
    public void printAbout() throws Exception {
        System.out.println("about = " + about);
    }

}