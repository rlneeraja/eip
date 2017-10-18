package gov.cdc.nczeid.eip.route.repository;

import org.springframework.data.repository.CrudRepository;

import gov.cdc.nczeid.eip.route.model.Route;

public interface RouteRepo extends CrudRepository<Route, String> {
	 public Route findByRguid(String rguid);
}
