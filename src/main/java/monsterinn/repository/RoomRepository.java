package monsterinn.repository;

import monsterinn.model.Room;
import monsterinn.model.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, String> {
    List<Room> findByElementCap(String elementCap);
    long countByStatus(RoomStatus status);
}
