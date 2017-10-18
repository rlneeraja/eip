package gov.cdc.nczeid.eip.routing.services;

import static io.restassured.RestAssured.given;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import gov.cdc.nczeid.eip.route.services.RoutingService;
import gov.cdc.nczeid.eip.uilt.LoadJson;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RoutingServiceTest {

    @Autowired
    private RoutingService service;

    @Before
    public void setUp() throws Exception {
    	service.init();
    }

//    public EIPMessage readJson(String filename) {
//        InputStream is = getClass().getClassLoader().getResourceAsStream(filename);
//        String content = new BufferedReader(new InputStreamReader(is)).lines().collect(Collectors.joining());
//        //get JSON:
//        Gson gson = new Gson();
//        return gson.fromJson(content, EIPMessage.class);
//    }

    @Test
    public void testSave() throws Exception {
    	service.save(LoadJson.readJson("minimumRoute.txt"));
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