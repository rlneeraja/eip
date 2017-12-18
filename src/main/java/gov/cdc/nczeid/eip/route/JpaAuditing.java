package gov.cdc.nczeid.eip.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Enables Spring Data auditing feature. 
 * The audit fields such as create date, update date, created by and modified by are set automatically by Spring Data.
 * @author hxo5
 *
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class JpaAuditing {

	@Bean
	public AuditorAware<String> auditorAware(){
		
		return new AuditAwareImpl();
	}
}
