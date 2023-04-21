
package acme.components;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.Banner;
import acme.framework.helpers.MomentHelper;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface BannerRepository extends AbstractRepository {

	@Query("select count(b) from Banner b where b.displayStartMoment <= :today AND b.displayEndMoment >= :today")
	int countBanners(Date today);

	@Query("select b from Banner b where b.displayStartMoment <= :today AND b.displayEndMoment >= :today")
	List<Banner> findManyBanners(Date today);

	default Banner findRandomBanner() {
		Banner result;
		int count, index;
		ThreadLocalRandom random;
		Date today;
		today = MomentHelper.getCurrentMoment();
		List<Banner> list;

		count = this.countBanners(today);
		if (count == 0)
			result = null;
		else {
			random = ThreadLocalRandom.current();
			index = random.nextInt(0, count);

			list = this.findManyBanners(today);
			result = list.isEmpty() ? null : list.get(index);
		}

		return result;
	}
}
