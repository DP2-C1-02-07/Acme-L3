
package acme.features.authenticated.company;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.framework.components.accounts.UserAccount;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Company;

@Repository
public interface AuthenticatedCompanyRepository extends AbstractRepository {

	@Query("Select ua From UserAccount ua Where ua.id = :id")
	UserAccount findOneUserAccountById(int id);

	@Query("Select c From Company c Where c.userAccount.id = :id")
	Company findOneCompanyByUserAccountId(int id);
}
