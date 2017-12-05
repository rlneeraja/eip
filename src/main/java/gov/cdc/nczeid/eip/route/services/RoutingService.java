package gov.cdc.nczeid.eip.route.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import gov.cdc.nczeid.eip.route.model.Route;
import gov.cdc.nczeid.eip.route.repository.RouteRepo;


@Service
public class RoutingService  {

    Map<String, String> routesMap = new  HashMap<String, String>();
    
    public Iterable<Route> routes ;
    
    @Autowired
    RouteRepo repo;
    /**
   	 * @Load files
   	 */
    @PostConstruct
    public void init() {
    	
    }

    public Map<String, String> getRoutesMap() {
    	routes = repo.findAll();
    	routes.forEach(
    			r -> {
    				routesMap.put(r.getCondition(), r.getDestination());
    			}
    			);
 		return routesMap;
 	}

 	public void setRoutesMap(Map<String, String> routesMap) {
 		this.routesMap = routesMap;
 	}
 	
    public Iterable<Route> getAll() {
        return repo.findAll();
    }

    public Route save(Route route) {
        return repo.save(route);
    }
    
    public Route findByRouteId(UUID rguid) {
        Route route = repo.findByRouteId(rguid);
        if (route != null) {
            return route;
        }
        return null;
    }
    
    public boolean routeExist(String condition, String destination) {
    	Iterable<Route> routes = repo.findByConditionAndDestination(condition, destination);
        return routes == null ? true:false;
    }
    
    
    public Iterable<Route> getRoutesByCondition(String condition){
    	return repo.findByCondition(condition);
    }

    
    public Iterable<Route> findByConditionAndDestination(String condition, String destination){
    	return repo.findByConditionAndDestination(condition, destination);
    }
   
}
