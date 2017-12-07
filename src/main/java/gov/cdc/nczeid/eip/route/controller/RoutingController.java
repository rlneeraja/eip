package gov.cdc.nczeid.eip.route.controller;

import gov.cdc.nczeid.eip.rest.ApiVersion;
import gov.cdc.nczeid.eip.route.model.ERROR_CODES;
import gov.cdc.nczeid.eip.route.model.ErrorResponse;
import gov.cdc.nczeid.eip.route.model.Route;
import gov.cdc.nczeid.eip.route.model.RouteApiResponse;
import gov.cdc.nczeid.eip.route.model._SWRoute;
import gov.cdc.nczeid.eip.route.services.EventSender;
import gov.cdc.nczeid.eip.route.services.RouteNotFoundException;
import gov.cdc.nczeid.eip.route.services.RoutingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@ApiVersion({1})
@CrossOrigin
public class RoutingController {

    private Log log = LogFactory.getLog(RoutingController.class);
    
    @Value("${default.createdBy}")
    private String createdBy;
    
    @Value("${default.updatedBy}")
    private String updatedBy;
  
    @Autowired
    private RoutingService routingService;

    @Autowired
    private EventSender eventSender;
    
    @ApiOperation(value = "Routes Message with guid to for a specific rabitmq queue for condition code", 
    		notes = "Routes Message with guid to for a specific rabitmq queue for condition code", 
    		response = RouteApiResponse.class,  produces="application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Routing successfully done"),
            @ApiResponse(code = 207, message = "Routing partially done with multi status"),
            @ApiResponse(code = 400, message = "Any error that is caused by the user - bad data sent, business rules violated, etc"),
            @ApiResponse(code = 403, message = "Caller is not authorized to execute this method"),
            @ApiResponse(code = 500, message = "Any Server side error can be reported via a 500. those errors are not recoverable by they user - something is wrong with the system")
    	}
    ) 
    @RequestMapping(value = "/routeMessage", method = POST)
	public ResponseEntity routeMessage(@Valid @RequestParam String condition, @RequestParam String mguid,
			HttpServletRequest request) throws Exception {
		// get the condition and queue map
		Iterable<Route> routes = routingService.getRoutesByCondition(condition);
		long size = routes.spliterator().getExactSizeIfKnown();
		
		RouteApiResponse apiresponse = new RouteApiResponse();
		apiresponse.setMguid(mguid);
		
		List destinations = new ArrayList<String> ();
		if (size > 0 ) {
			Iterator<Route> itRoutes = routes.iterator();
			while (itRoutes.hasNext()) {
				Route r = itRoutes.next();
				if (r.getDestination() != null && !r.getDestination().isEmpty()){
						eventSender.send(r.getDestination(), mguid);
						destinations.add(r.getDestination());
				}
			}
			apiresponse.setQueName(destinations);
		} else {
			ErrorResponse error = new ErrorResponse( ERROR_CODES.UNPROCESSABLE_ENTITY,
					"No Routes defined for condition. Please try again later or contact EIP's system administrator to get the route created for condition code",
					request.getRequestURL().toString(), 400, "");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(apiresponse);
	}

    @ApiOperation(value = "Get Route resource for a soecific routeId", 
    		notes = "Get Route resource for a soecific routeId", 
    		response = _SWRoute.class, produces="application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Registration successfully created"),
            @ApiResponse(code = 400, message = "Any error that is caused by the user - bad data sent, business rules violated, etc"),
            @ApiResponse(code = 403, message = "Caller is not authorized to execute this method"),
            @ApiResponse(code = 409, message = "Conflict"),
            @ApiResponse(code = 500, message = "Any Server side error can be reported via a 500. those errors are not recoverable by they user - something is wrong with the system")
    	}
    )
    @RequestMapping(value="/route/{routeId}", method = GET)
    public ResponseEntity getRoute(@PathVariable UUID routeId,HttpServletRequest request) throws RouteNotFoundException, Exception{
        Route route = routingService.findByRouteId(routeId);
            return ResponseEntity.ok(route);
    }
    
    @ApiOperation(value = "Get all Route resources", 
    		notes = "Get all Route resources", 
    		response = _SWRoute.class, produces="application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Registration successfully created"),
            @ApiResponse(code = 400, message = "Any error that is caused by the user - bad data sent, business rules violated, etc"),
            @ApiResponse(code = 403, message = "Caller is not authorized to execute this method"),
            @ApiResponse(code = 409, message = "Conflict"),
            @ApiResponse(code = 500, message = "Any Server side error can be reported via a 500. those errors are not recoverable by they user - something is wrong with the system")
    	}
    )
    @RequestMapping(value="/routes", method = GET)
    public ResponseEntity getRoutes(HttpServletRequest request) throws  Exception {
        Iterable<Route> routes = routingService.getAll();
        return ResponseEntity.ok(routes);
    }

    @ApiOperation(value = "Create a Route resources", 
    		notes = "Create a Route resources", 
    		response = _SWRoute.class, produces="application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Route successfully created"),
            @ApiResponse(code = 400, message = "Any error that is caused by the user - bad data sent, business rules violated, etc"),
            @ApiResponse(code = 403, message = "Caller is not authorized to execute this method"),
            @ApiResponse(code = 409, message = "Conflict"),
            @ApiResponse(code = 500, message = "Any Server side error can be reported via a 500. those errors are not recoverable by they user - something is wrong with the system")
    	}
    )
    @RequestMapping(value = "/route", method = POST)
    public ResponseEntity create(@Valid @RequestBody Route route, HttpServletRequest request)  throws  Exception{
    	Iterable<Route> existingRoute = routingService.findByConditionAndDestination(route.getCondition(), route.getDestination());
    	long size = existingRoute.spliterator().getExactSizeIfKnown();
    	if(route.getRouteId() == null && size == 0 ){
    		Route r = routingService.save(route);
	       	return ResponseEntity.status(HttpStatus.CREATED).body(r);
    	}
    	else{
    		ErrorResponse error = new ErrorResponse( ERROR_CODES.BAD_REQUEST, "Route already exist and can not be created", request.getRequestURL().toString() , 409, "");
       	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    	}
    		
    }
    
    @ApiOperation(value = "Update a Route resources", 
    		notes = "Update a Route resources", 
    		response = _SWRoute.class, produces="application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Route successfully updated"),
            @ApiResponse(code = 400, message = "Any error that is caused by the user - bad data sent, business rules violated, etc"),
            @ApiResponse(code = 403, message = "Caller is not authorized to execute this method"),
            @ApiResponse(code = 409, message = "Conflict"),
            @ApiResponse(code = 500, message = "Any Server side error can be reported via a 500. those errors are not recoverable by they user - something is wrong with the system")
    	}
    )
    @RequestMapping(value = "/route", method = PUT)
    public ResponseEntity update(@Valid @RequestBody Route route, HttpServletRequest request)  throws  Exception{
	       	 	Route r = routingService.save(route);
		        return ResponseEntity.status(HttpStatus.OK).body(r);
    		
    }
    
    @ApiOperation(value = "Delete a Route resource", 
    		notes = "Delete a Route resource", 
    		response = _SWRoute.class, produces="application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Route successfully deleted"),
            @ApiResponse(code = 400, message = "Any error that is caused by the user - bad data sent, business rules violated, etc"),
            @ApiResponse(code = 403, message = "Caller is not authorized to execute this method"),
            @ApiResponse(code = 409, message = "Conflict"),
            @ApiResponse(code = 500, message = "Any Server side error can be reported via a 500. those errors are not recoverable by they user - something is wrong with the system")
    	}
    )
    @RequestMapping(value = "/route/{routeId},{userSuppliedVersion}", method = PUT)
    public ResponseEntity delete(@PathVariable UUID routeId, @PathVariable Long userSuppliedVersion,HttpServletRequest request) throws RouteNotFoundException, Exception {
    		Route route = routingService.findByRouteId(routeId);
    		if(route.getVersion() == userSuppliedVersion){
	    		if(route.getActive() == 1){
	    			route.setActive(0);
		    		route = routingService.save(route);
		        	return ResponseEntity.status(HttpStatus.OK).body(route);
	        	}
		    	else{
		    		ErrorResponse error = new ErrorResponse( ERROR_CODES.CONFLICT, "Route is already marked deleted", request.getRequestURL().toString() , 409, "");
		       	    return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
		        }
    		}
    		else{
    			ErrorResponse error = new ErrorResponse( ERROR_CODES.CONFLICT, "Version mismatch on the route, Please refresh and try again.", request.getRequestURL().toString() , 409, "");
	       	    return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    		}
    			
    		
    }
    
    @ApiOperation(hidden=true, value = "Exception Handling")
    @ExceptionHandler({HttpMessageNotReadableException.class, HttpMediaTypeNotSupportedException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @RequestMapping("/errormessage")
    private ResponseEntity<ErrorResponse> handleParameterError(HttpServletRequest request, Exception e) {
    	ErrorResponse error = new ErrorResponse( ERROR_CODES.BAD_REQUEST, "Missing body with message to be processed", request.getRequestURL().toString() , 400, e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ApiOperation(hidden=true, value = "Exception Handling")
    @ExceptionHandler({MethodArgumentTypeMismatchException.class, RouteNotFoundException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @RequestMapping("/notfound")
    private ResponseEntity<ErrorResponse> handleNotFoundError(HttpServletRequest request, Exception e) {
    	ErrorResponse error = new ErrorResponse( ERROR_CODES.BAD_REQUEST, "Invalid Rguid Passed", request.getRequestURL().toString() , 400, e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    
    @ApiOperation(hidden=true, value = "Exception Handling")
    @ExceptionHandler({Exception.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @RequestMapping("/serverError")
    public ResponseEntity<ErrorResponse> noHandlerFoundException(HttpServletRequest request, Exception e) {
    	ErrorResponse error = new ErrorResponse( ERROR_CODES.INTERNAL_SERVER_ERROR, "We are unable to process your request at this moment. The message has not been accepted. Please try again later or contact EIP's system administrator", request.getRequestURL().toString() , 500, e.getMessage());
	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
    
    @ApiOperation(hidden=true, value = "Exception Handling")
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
