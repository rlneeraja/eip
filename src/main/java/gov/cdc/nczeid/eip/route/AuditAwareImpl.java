package gov.cdc.nczeid.eip.route;

import org.springframework.data.domain.AuditorAware;

public class AuditAwareImpl implements AuditorAware<String> {

	@Override
	public String getCurrentAuditor() {
		return "EIP_ROUTING_SERVICE";
	}

}
