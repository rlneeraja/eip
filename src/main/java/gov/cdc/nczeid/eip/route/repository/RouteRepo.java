package gov.cdc.nczeid.eip.route.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import gov.cdc.nczeid.eip.route.model.Route;

public interface RouteRepo extends CrudRepository<Route, String> {
	 public Route findByRguid(String rguid);
	
//	 @Query("MATCH Route (destination:destination) RETURN r")
//	 MATCH (wallstreet:Movie { title: 'Wall Street' })<-[:ACTED_IN]-(actor)
//	 RETURN actor.name
//	 public Route findByDestination(String destination);
//	 
	 @Query("SELECT r FROM Route r WHERE r.destination = ?")
	 public Route findByDestination(String destination);
	 
}
