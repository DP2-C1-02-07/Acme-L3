
package acme.features.auditor;

import org.springframework.stereotype.Service;

import acme.entities.Audit;
import acme.framework.services.AbstractService;
import acme.roles.Auditor;

@Service
public class AuditorAuditDeleteService extends AbstractService<Auditor, Audit> {

}
