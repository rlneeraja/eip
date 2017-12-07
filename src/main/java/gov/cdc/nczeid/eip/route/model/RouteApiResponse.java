package gov.cdc.nczeid.eip.route.model;


import java.util.List;

import lombok.Data;

@Data
public class RouteApiResponse {
    private List queName;
    private String  mguid;
    private String message = "Successfuly Routed";

    public RouteApiResponse() {
    }

    
  
    
}
