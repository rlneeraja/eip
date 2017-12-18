package gov.cdc.nczeid.eip.route.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import gov.cdc.nczeid.eip.route.model.Route;
@Repository
public interface RouteRepo extends JpaRepository<Route, String> {
	 
	public  Route findByRouteId(String routeId);
	
	public  Route findByDestination(String destination);
	  
	@Query("FROM Route where condition =:condition and destination =:destination")
	public Iterable<Route> findByConditionAndDestination(@Param("condition") String condition, @Param("destination") String destination);
	 
	@Query("FROM Route where condition =:condition")
	public Iterable<Route> findByCondition(@Param("condition") String condition);
	 
}
