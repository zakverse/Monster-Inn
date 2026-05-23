package monsterinn.modules.room.repository;

import monsterinn.modules.room.model.Room;
import monsterinn.modules.room.model.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, String> {
    List<Room> findByElementCap(String elementCap);
    long countByStatus(RoomStatus status);
}
