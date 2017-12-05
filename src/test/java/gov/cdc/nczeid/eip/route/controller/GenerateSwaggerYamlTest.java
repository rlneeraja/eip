package gov.cdc.nczeid.eip.route.controller;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.google.common.base.Charsets;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
@TestPropertySource(locations="classpath:application.yml")
public class GenerateSwaggerYamlTest {
    @Value("${server.apiContext}")
    private String apiContext;
    
    @Value("${server.context-path}")
    private String contextPath;

    
    @Value("${server.versionContext}")
    private String versionContext;
    
    @Value("${server.port}")
    private String port;
    
    private String rootAPIIURL;

    @Before
    public void setUp() throws Exception {
        this.rootAPIIURL = "http://localhost:10001"+
        		contextPath+"/"+apiContext+ "/"+  versionContext+"2/api-docs";
    }
	@Test
	public void generate() throws IOException{
		InputStream in = new URL(rootAPIIURL).openStream();
		String jsonString = IOUtils.toString(in, Charsets.UTF_8);    
	    JsonNode jsonTree = new ObjectMapper().readTree(jsonString);
	    String yaml = new YAMLMapper().writeValueAsString(jsonTree);
	    
	    FileWriter writer = new FileWriter("./src/docs/swaggerTest.yaml");
	    writer.write(yaml);
	    writer.close();
	}
}
