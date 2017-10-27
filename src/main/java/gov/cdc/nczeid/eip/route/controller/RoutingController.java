package gov.cdc.nczeid.eip.route.controller;



import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import gov.cdc.nczeid.eip.rest.ApiVersion;
import gov.cdc.nczeid.eip.route.model.ERROR_CODES;
import gov.cdc.nczeid.eip.route.model.ErrorResponse;
import gov.cdc.nczeid.eip.route.model.Route;
import gov.cdc.nczeid.eip.route.services.EventSender;
import gov.cdc.nczeid.eip.route.services.RoutingService;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/route")
@ApiVersion({1})
@CrossOrigin
public class RoutingController {

    private Log log = LogFactory.getLog(RoutingController.class);
  
    @Autowired
    private RoutingService routingService;

    @Autowired
    private EventSender eventSender;
    
    @RequestMapping(value = "/routeMessage/{condition},{mguid}", method = GET)
    public ResponseEntity routeMessage(@PathVariable String condition, @PathVariable String mguid , HttpServletRequest request)  {
    	String routedTo = "";
    	try {
        	// get the condition and queue map
    		
        	Map<String, String> rtMap = routingService.getRoutesMap();
        	if(rtMap != null && !rtMap.isEmpty()){
        		routedTo = rtMap.get(condition);
        		eventSender.send(rtMap.get(condition), mguid);
        		if(routedTo != null && routedTo.isEmpty()){
        			ErrorResponse error = new ErrorResponse(new Date(), ERROR_CODES.NOT_ACCEPTABLE, "The route for the condition code is not defined. Please try again later or contact EIP's system administrator to get the route created for condition code", request.getRequestURL().toString() , 500, "The route for the condition code is not defined. Please try again later or contact EIP's system administrator to get the route created for condition code");
         		    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(error);
        		}
        			
        	}
        	else{
        		ErrorResponse error = new ErrorResponse(new Date(), ERROR_CODES.NOT_ACCEPTABLE, "The route for the condition code is not defined. Please try again later or contact EIP's system administrator to get the route created for condition code", request.getRequestURL().toString() , 500, "The route for the condition code is not defined. Please try again later or contact EIP's system administrator to get the route created for condition code");
     		    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(error);
        	}
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Message with guid " + mguid + " is routed to queue " + routedTo);
        } catch (Exception e) {
            log.error("exception: " + e.getMessage());
            e.printStackTrace(); 
            ErrorResponse error = new ErrorResponse(new Date(), ERROR_CODES.INTERNAL_SERVER_ERROR, "We are unable to process your request at this moment. The message has not been accepted. Please try again later or contact EIP's system administrator", request.getRequestURL().toString() , 500, e.getMessage());
		    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @RequestMapping(value="/{rguid}", method = GET)
    public ResponseEntity getRoute(@PathVariable String rguid,HttpServletRequest request) {
        Route route = routingService.findByRguid(rguid);
        if (route != null) {
            return ResponseEntity.ok(route);
        } else {
        	 ErrorResponse error = new ErrorResponse(new Date(), ERROR_CODES.NOT_FOUND, "Message with guid " + rguid + " not found", request.getRequestURL().toString() , 500, "");
        	 return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
    @RequestMapping(value="/routes", method = GET)
    public ResponseEntity getRoutes(HttpServletRequest request) {
        Iterable<Route> routes = routingService.getAll();
        if (routes != null ) {
            return ResponseEntity.ok(routes);
        } else {
            ErrorResponse error = new ErrorResponse(new Date(), ERROR_CODES.NOT_FOUND, "Routes not defined yet", request.getRequestURL().toString() , 500, "");
       	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @RequestMapping(value = "", method = POST)
    public ResponseEntity save(@RequestBody Route route, HttpServletRequest request) {
    	try{
    		if(routingService.routeExist(route.getDestination())){
	    		Route r = routingService.save(route);
	        	return ResponseEntity.status(HttpStatus.OK).body(r.getRguid());
        	}
	    	else{
	    		ErrorResponse error = new ErrorResponse(new Date(), ERROR_CODES.CONFLICT, "Route Destination already defined", request.getRequestURL().toString() , 409, "");
	       	    return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
	        }
	    			
        }
    	catch(Exception e) {
    		ErrorResponse error = new ErrorResponse(new Date(), ERROR_CODES.INTERNAL_SERVER_ERROR, "We are unable to process your request at this moment. The message has not been accepted. Please try again later or contact EIP's system administrator", request.getRequestURL().toString() , 500, e.getMessage());
		    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

}
