package gov.cdc.nczeid.eip.route.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import gov.cdc.nczeid.eip.rest.ApiVersion;
import gov.cdc.nczeid.eip.route.About;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by Neeraja on 10/4/17.
 */

@RestController
@RequestMapping("/info/")
@ApiVersion({1})
@CrossOrigin
public class InfoServiceController {
    @Autowired
    private About about;

    @Value("${server.apiContext}")
    private String apiContext;

    @Value("${server.versionContext}")
    private String versionContext;


    @RequestMapping(value = "/about", method = GET)
    // produces = {"application/cdc.info.about-v1+json"}) //This forces Safari to download the file instead of opening it on the browser.
    @ResponseBody
    public About about() throws JsonProcessingException {
        return about;
    }

    @RequestMapping(value="/ping",  method  =  GET)
    public String ping() {
        return "Hello There! I'm alive\nYou pinged me at " + ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT);
    }


    @RequestMapping(value="/version",  method  =  GET)
    public  String  getVersion() {
        return  "Version: "  + getClass().getPackage().getImplementationVersion();
    }

}

