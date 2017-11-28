package gov.cdc.nczeid.eip.route.controller;



import gov.cdc.nczeid.eip.rest.ApiVersion;
import gov.cdc.nczeid.eip.route.model.ERROR_CODES;
import gov.cdc.nczeid.eip.route.model.ErrorResponse;
import gov.cdc.nczeid.eip.route.model.Route;
import gov.cdc.nczeid.eip.route.services.EventSender;
import gov.cdc.nczeid.eip.route.services.RouteNotFoundException;
import gov.cdc.nczeid.eip.route.services.RoutingService;
import gov.cdc.nczeid.eip.utils.InputStreamUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@ApiVersion({1})
@CrossOrigin
public class RoutingController {

    private Log log = LogFactory.getLog(RoutingController.class);
  
    @Autowired
    private RoutingService routingService;

    @Autowired
    private EventSender eventSender;
    
    @RequestMapping(value = "/routeMessage", method = POST)
	public ResponseEntity routeMessage(@Valid @RequestParam String condition, @RequestParam String mguid,
			HttpServletRequest request) throws Exception {
		String routedTo = "";
		// get the condition and queue map
		Iterable<Route> routes = routingService.getRoutesByCondition(condition);
		if (routes != null) {
			Iterator<Route> itRoutes = routes.iterator();
			while (itRoutes.hasNext()) {
				Route r = itRoutes.next();
				if (r.getDestination() != null && !r.getDestination().isEmpty())
					eventSender.send(condition, mguid);
			}
		} else {
			ErrorResponse error = new ErrorResponse( ERROR_CODES.UNPROCESSABLE_ENTITY,
					"No Routes defined for condition. Please try again later or contact EIP's system administrator to get the route created for condition code",
					request.getRequestURL().toString(), 422, "");
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
		}
		return ResponseEntity.status(HttpStatus.ACCEPTED)
				.body("Message with guid " + mguid + " is routed to queue " + routedTo);
	}

    @RequestMapping(value="/route/{routeId}", method = GET)
    public ResponseEntity getRoute(@PathVariable UUID routeId,HttpServletRequest request) throws RouteNotFoundException, Exception{
        Route route = routingService.findByRouteId(routeId);
            return ResponseEntity.ok(route);
    }
    
    @RequestMapping(value="/routes", method = GET)
    public ResponseEntity getRoutes(HttpServletRequest request) throws  Exception {
        Iterable<Route> routes = routingService.getAll();
        return ResponseEntity.ok(routes);
    }

    @RequestMapping(value = "/route", method = POST)
    public ResponseEntity save(@Valid @RequestBody Route route, HttpServletRequest request)  throws  Exception{
    	if(route.getVersion() == null && route.getRouteId() != null){
    		ErrorResponse error = new ErrorResponse( ERROR_CODES.CONFLICT, "Route can be updated on an existing version", request.getRequestURL().toString() , 409, "");
       	    return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    	}
    	else{
       	 	Route r = routingService.save(route);
	        return ResponseEntity.status(HttpStatus.OK).body(r);
    	}
    		
    }
    
    @RequestMapping(value = "/route/{routeId}", method = PUT)
    public ResponseEntity delete(@PathVariable UUID routeId, HttpServletRequest request) throws RouteNotFoundException, Exception {
    		Route route = routingService.findByRouteId(routeId);
    		
    		if(route.getActive() == 1){
    			route.setActive(0);
	    		route = routingService.save(route);
	        	return ResponseEntity.status(HttpStatus.OK).body(route);
        	}
	    	else{
	    		ErrorResponse error = new ErrorResponse( ERROR_CODES.CONFLICT, "Route is already inactive", request.getRequestURL().toString() , 409, "");
	       	    return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
	        }
    }
    
  
    @ExceptionHandler({HttpMessageNotReadableException.class, HttpMediaTypeNotSupportedException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @RequestMapping("/errormessage")
    private ResponseEntity<ErrorResponse> handleParameterError(HttpServletRequest request, Exception e) {
    	ErrorResponse error = new ErrorResponse( ERROR_CODES.BAD_REQUEST, "Missing body with message to be processed", request.getRequestURL().toString() , 400, e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, RouteNotFoundException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @RequestMapping("/notfound")
    private ResponseEntity<ErrorResponse> handleNotFoundError(HttpServletRequest request, Exception e) {
    	ErrorResponse error = new ErrorResponse( ERROR_CODES.BAD_REQUEST, "Invalid Rguid Passed", request.getRequestURL().toString() , 400, e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    
    @ExceptionHandler({Exception.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @RequestMapping("/serverError")
    public ResponseEntity<ErrorResponse> noHandlerFoundException(HttpServletRequest request, Exception e) {
    	ErrorResponse error = new ErrorResponse( ERROR_CODES.INTERNAL_SERVER_ERROR, "We are unable to process your request at this moment. The message has not been accepted. Please try again later or contact EIP's system administrator", request.getRequestURL().toString() , 500, e.getMessage());
	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
    
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    @RequestMapping("/validatonError")
    protected ResponseEntity<ErrorResponse> resolveValidationErrors(HttpServletRequest request, MethodArgumentNotValidException ex) {
        log.error("Missing Required info...");
        BindingResult result =  ex.getBindingResult();
        List<String>  errorMessages = new ArrayList<>();
        for( FieldError f: result.getFieldErrors()) {
            errorMessages.add(f.getDefaultMessage());
        }

        ErrorResponse error = new ErrorResponse(ERROR_CODES.UNPROCESSABLE_ENTITY, "Required inputs not defined", request.getRequestURL().toString() , 422, "");
   	   
        error.setDetails(errorMessages.toArray());
        return ResponseEntity.badRequest().body(error);

    }

    
   
}
