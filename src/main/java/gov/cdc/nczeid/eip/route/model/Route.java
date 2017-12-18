package gov.cdc.nczeid.eip.route.model;


import lombok.Data;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.Valid;

import java.util.Date;
import java.util.UUID;



@Data
@Entity(name="Route")
@JsonPropertyOrder({"code","name","active","createdBy","createdTime","updatedBy","updatedTime","version"})
public class Route extends AuditFields{
	
    @Id
    @Column(name="Route_id", nullable = false, unique = true)
    private String routeId;
    
    @ApiModelProperty(required=true, value="Condition Code", example="10001", position=2) 
    @NotEmpty(message="Condition can not be empty")
    @Column(name="Condition", nullable = false)
    private String condition;
    
    @ApiModelProperty(required=true, value="Destination Route Name", example="FDD", position=3)
    @NotEmpty(message="Destination can not be empty")
    @Column(name="Destination")
    private String destination;
    
    @Column(name="Active")
    @ApiModelProperty(required=true, hidden=true, position=95)
    private Integer active = 1;
    
}
