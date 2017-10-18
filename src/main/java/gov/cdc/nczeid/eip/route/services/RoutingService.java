package gov.cdc.nczeid.eip.route.services;

import com.google.gson.Gson;

import gov.cdc.nczeid.eip.route.model.Route;
import gov.cdc.nczeid.eip.route.repository.RouteRepo;

import java.util.Collections;
import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class RoutingService {

    @Autowired
    private RouteRepo repo;
    
    HashMap<String, String> routesMap = new  HashMap<String, String>();
    
    
    /**
   	 * @Load files
   	 */
    @PostConstruct
    public void init() {
    	Iterable<Route> routes = repo.findAll();
    	routes.forEach(
    			r -> {
    				routesMap.put(r.getCondition(), r.getDestionation());
    			}
    			);
    }

    public HashMap<String, String> getRoutesMap() {
 		return routesMap;
 	}

 	public void setRoutesMap(HashMap<String, String> routesMap) {
 		this.routesMap = routesMap;
 	}
 	
    private Gson gson = new Gson();


    public String save(Route route) {
        Route saved =  repo.save(route);
        return saved.getRguid();
    }
    
    public Iterable<Route> getAll() {
        return repo.findAll();
    }

    public Route findByRguid(String rguid) {
        Route route = repo.findByRguid(rguid);
        if (route != null) {
            return route;
        }
        return null;
    }
}
