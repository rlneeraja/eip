package gov.cdc.nczeid.eip.route.model;


import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.Valid;

import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table(name="Route")
public class Route {
	
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name="Route_id", nullable = false, unique = true)
    private UUID routeId;
    @Version
    @Column(name="Version", columnDefinition = "integer DEFAULT 0", nullable = false)
    private Integer version;
    @Valid
    @NotEmpty(message="Condition can not be empty")
    @Column(name="Condition", nullable = false)
    private String condition;
    @Valid
    @NotEmpty(message="Destination can not be empty")
    @Column(name="Destination")
    private String destination;
    @Column(name="Active")
    private Integer active = 1;
    @Column(name="created_datetime")
    private Date createdDateTime;
    @Column(name="update_datetime")
    private Date updateDateTime;
    @Column(name="created_by")
    private String createdBy = "EIP_ROUTING_SERVICE";
    @Column(name="updated_by")
    private String updatedBy = "EIP_ROUTING_SERVICE";

}
