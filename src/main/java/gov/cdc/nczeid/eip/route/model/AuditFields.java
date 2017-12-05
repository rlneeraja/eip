package gov.cdc.nczeid.eip.route.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * Audit fields abstract class inherited by other model classes. 
 * @CreatedDate, @LastModifiedDate are populated by Spring Data
 * @CretedBy, @LastModifiedBy are populated by AuditAwareImpl class using Spring Data 
 * @JsonIgnore ignores the fields being populated by @RequestBody by Jackson and Swagger docs
 * 
 * @author hxo5
 *
 */
@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditFields {
	@ApiModelProperty(hidden=true, position=90)
	@CreatedBy
	@Column (name="created_by",nullable = false, length = 75, updatable= false)
	private String createdBy;
	
	@ApiModelProperty(hidden=true, position=91)
	@CreatedDate
	@Column (name="created_datetime",nullable = false, length = 75, updatable= false)
	private Date createdTime;
	
	@ApiModelProperty(hidden=true, position=92)
	@LastModifiedBy
	@Column (name="updated_by")
	private String updatedBy;
	
	@ApiModelProperty(hidden=true, position=93)
	@LastModifiedDate
	@Column (name="update_datetime")
	private Date updatedTime;
	
	@ApiModelProperty(hidden=true, position=94)
    @Version
    @Column(name="version", columnDefinition = "integer DEFAULT 0", nullable = false)
    private long version = 0L;
}
