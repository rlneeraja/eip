package gov.cdc.nczeid.eip.route.repository;

import org.springframework.data.repository.CrudRepository;

import gov.cdc.nczeid.eip.route.model.Route;

public interface RouteRepo extends CrudRepository<Route, String> {
	 public Route findByRouteId(String routeId);
	
	 //@Query("SELECT r FROM Route r WHERE r.destination = ?")
	  Route findByDestination(String destination);
	 
}
