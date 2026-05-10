package monsterinn.modules.room.repository;

import monsterinn.modules.room.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, String> {
    // Cari kamar berdasarkan status (Available/Occupied)
    List<Room> findByStatus(String status);
}