package gov.cdc.nczeid.eip.route.model;


import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name="Route")
public class Route {
	
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name="rguid", nullable = false, unique = true)
    private String rguid;
    @Column(name="Version")
    private String version;
    @Column(name="Condition", nullable = false)
    private String condition;
    @Column(name="Destionation")
    private String destination;
    @Column(name="created_datetime")
    private Date createdDateTime;
    @Column(name="update_datetime")
    private Date updateDateTime;
    @Column(name="created_by")
    private String createdBy = "EIP_ROUTING_SERVICE";
    @Column(name="updated_by")
    private String updatedBy = "EIP_ROUTING_SERVICE";

}
