package monsterinn.modules.monster.repository;

import monsterinn.modules.monster.model.Monster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonsterRepository extends JpaRepository<Monster, String> {
    List<Monster> findByRoomIdIsNotNull();
}
