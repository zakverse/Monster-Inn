package monsterinn.modules.monster.repository;

import monsterinn.modules.monster.model.Monster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonsterRepository extends JpaRepository<Monster, String> {
    // Karena ID Monster lu pake String (Contoh: M-001)
}