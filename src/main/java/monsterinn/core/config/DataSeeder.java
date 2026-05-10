package monsterinn.core.config; // Package disesuaikan ke folder core.config

import monsterinn.modules.monster.model.*;
import monsterinn.modules.room.model.*;
import monsterinn.modules.service.model.ServiceEntity;
import monsterinn.modules.room.repository.RoomRepository;
import monsterinn.modules.monster.repository.MonsterRepository;
import monsterinn.modules.service.repository.ServiceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initDatabase(
            RoomRepository roomRepo, 
            MonsterRepository monsterRepo,
            ServiceRepository serviceRepo) {
        return args -> {

            // 1. DATA KAMAR
            if (roomRepo.count() == 0) {
                roomRepo.saveAll(List.of(
                    new Room("101", "Api", 150000),
                    new Room("201", "Air", 175000),
                    new Room("301", "Tanah", 120000)
                ));
            }

            // 2. DATA MENU LAYANAN
            if (serviceRepo.count() == 0) {
                serviceRepo.saveAll(List.of(
                    new ServiceEntity("Magma Injection", 60000.0, "Api", "fa-fire"),
                    new ServiceEntity("Lava Bath", 75000.0, "Api", "fa-hot-tub-person"),
                    new ServiceEntity("Aquatic Nutrient", 70000.0, "Air", "fa-droplet"),
                    new ServiceEntity("Deep Sea Mist", 45000.0, "Air", "fa-cloud-showers-heavy"),
                    new ServiceEntity("Soil Nutrient", 50000.0, "Tanah", "fa-leaf")
                ));
            }

            // 3. DATA TAMU AKTIF
            if (monsterRepo.count() == 0) {
                FireMonster ember = new FireMonster("M-001", "Ember Blaze", 50000.0, 15000.0);
                ember.setRoomId("101");
                monsterRepo.save(ember);

                WaterMonster wade = new WaterMonster("M-002", "Wade Ripple", 55000.0, 20000.0);
                wade.setRoomId("201");
                monsterRepo.save(wade);
            }
            
            System.out.println("✦ [CORE] Data Seeder Berhasil Dijalankan di folder Config!");
        };
    }
}