package gov.cdc.nczeid.eip.route.services;

import com.google.gson.Gson;

import gov.cdc.nczeid.eip.route.model.Route;
import gov.cdc.nczeid.eip.route.repository.RouteRepo;

import java.util.Collections;
import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;


@Service
public class RoutingService  {

    HashMap<String, String> routesMap = new  HashMap<String, String>();
    
    @Autowired
    RouteRepo repo;
    /**
   	 * @Load files
   	 */
    @PostConstruct
    public void init() {
    	Iterable<Route> routes = repo.findAll();
    	routes.forEach(
    			r -> {
    				routesMap.put(r.getCondition(), r.getDestination());
    			}
    			);
    }

    public HashMap<String, String> getRoutesMap() {
    	init();
 		return routesMap;
 	}

 	public void setRoutesMap(HashMap<String, String> routesMap) {
 		this.routesMap = routesMap;
 	}
 	
    private Gson gson = new Gson();

    
    public Iterable<Route> getAll() {
        return repo.findAll();
    }

    public Route save(Route route) {
        return repo.save(route);
    }
    
    public Route findByRguid(String rguid) {
        Route route = repo.findByRguid(rguid);
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
