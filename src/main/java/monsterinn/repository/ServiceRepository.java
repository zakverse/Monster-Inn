package monsterinn.repository;

import monsterinn.model.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {
    // Repository ini sekarang sah memegang kendali tabel MASTER MENU SERVICES!
}