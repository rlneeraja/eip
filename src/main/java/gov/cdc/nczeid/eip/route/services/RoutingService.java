package gov.cdc.nczeid.eip.route.services;

import java.util.HashMap;
import java.util.Map;

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
    @Cacheable("routes")
    public void init() {
    	routes = repo.findAll();
    	routes.forEach(
    			r -> {
    				routesMap.put(r.getCondition(), r.getDestination());
    			}
    			);
    }

    public Map<String, String> getRoutesMap() {
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
    
    public Route findByRouteId(String rguid) {
        Route route = repo.findByRouteId(rguid);
        if (route != null) {
            return route;
        }
        return null;
    }
    
    public boolean routeExist(String destination) {
        Route route = repo.findByDestination(destination);
        return route == null ? true:false;
    }
    
   
}
