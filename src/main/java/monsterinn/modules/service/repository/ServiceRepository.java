package monsterinn.modules.service.repository;
import monsterinn.modules.service.model.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {}