// package monsterinn.core.config;

// import monsterinn.modules.monster.model.*;
// import monsterinn.modules.room.model.*;
// import monsterinn.modules.room.repository.RoomRepository;
// import monsterinn.modules.monster.repository.MonsterRepository;
// import org.springframework.boot.CommandLineRunner;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

// @Configuration
// public class DataSeeder {

//     @Bean
//     CommandLineRunner initDatabase(RoomRepository roomRepo, MonsterRepository monsterRepo) {
//         return args -> {
//             // 1. Generate 15 Kamar (5 per elemen)
//             if (roomRepo.count() == 0) {
//                 for (int i = 1; i <= 5; i++) {
//                     roomRepo.save(new Room("10" + i, "FIRE", 150000));
//                     roomRepo.save(new Room("20" + i, "WATER", 175000));
//                     roomRepo.save(new Room("30" + i, "EARTH", 120000));
//                 }
//                 System.out.println("✦ [SEEDER] 15 Kamar Berhasil Dibuat!");
//             }

//             // 2. Data Tamu Awal
//             if (monsterRepo.count() == 0) {
//                 // Ember Blaze -> Kamar 101 (FIRE)
//                 FireMonster ember = new FireMonster("M-001", "Ember Blaze", 50000.0, 15000.0);
//                 Room r101 = roomRepo.findById("101").orElse(null);
//                 if (r101 != null) {
//                     r101.checkIn(ember);
//                     monsterRepo.save(ember);
//                     roomRepo.save(r101);
//                 }

//                 // Wade Ripple -> Kamar 201 (WATER)
//                 WaterMonster wade = new WaterMonster("M-002", "Wade Ripple", 55000.0, 20000.0);
//                 Room r201 = roomRepo.findById("201").orElse(null);
//                 if (r201 != null) {
//                     r201.checkIn(wade);
//                     monsterRepo.save(wade);
//                     roomRepo.save(r201);
//                 }
//                 System.out.println("✦ [SEEDER] Tamu Berhasil Check-in!");
//             }
//         };
//     }
// }