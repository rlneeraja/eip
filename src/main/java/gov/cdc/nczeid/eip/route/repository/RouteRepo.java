package gov.cdc.nczeid.eip.route.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import gov.cdc.nczeid.eip.route.model.Route;
@Repository
public interface RouteRepo extends CrudRepository<Route, UUID> {
	 
	public  Route findByRouteId(UUID routeId);
	
	public  Route findByDestination(String destination);
	  
	@Query("FROM Route where condition =:condition and destination =:destination")
	public List<Route> findByConditionAndDestination(@Param("condition") String condition, @Param("destination") String destination);
	 
}
