package gov.cdc.nczeid.eip.route.model;


import lombok.Data;

@Data
public class ApiResponse {
    private String queName;
    private String message = "Successfuly Routed";

    public ApiResponse() {
    }

    public ApiResponse(String queName) {
        this.queName = queName;
    }
}
