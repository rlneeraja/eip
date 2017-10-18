package gov.cdc.nczeid.eip.route.model;


import lombok.Data;

@Data
public class DeleteResponse {
    private String rguid;
    private String status = "Delete Success";

    public DeleteResponse() {
    }

    public DeleteResponse(String rguid) {
        this.rguid = rguid;
    }
}
