package monsterinn.modules.room.repository;

import monsterinn.modules.room.model.Room;
import monsterinn.modules.room.model.RoomStatus; // Import Enum-nya
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, String> {
    // Cari kamar berdasarkan tipe data Enum RoomStatus agar tidak error
    List<Room> findByStatus(RoomStatus status);
}