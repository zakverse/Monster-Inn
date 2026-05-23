package monsterinn.modules.service.repository;

import monsterinn.modules.service.model.ServiceRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceRequest, Integer> {
    List<ServiceRequest> findByGuestId(String guestId);
    List<ServiceRequest> findByRoomId(String roomId);
}
