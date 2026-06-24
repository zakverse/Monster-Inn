--=============================================
-- DATA SEEDER SCRIPT
----=============================================

-- ============================================
-- Monster Inn — Initial Seed Data
-- 9 Rooms: 3 Fire, 3 Water, 3 Earth
-- ============================================

-- Fire Habitat Rooms
INSERT INTO rooms (room_id, element_cap, room_rate, is_occupied, status, guest_id)
VALUES ('F-101', 'FIRE', 150000, false, 'AVAILABLE', NULL)
ON DUPLICATE KEY UPDATE element_cap = VALUES(element_cap), room_rate = VALUES(room_rate);

INSERT INTO rooms (room_id, element_cap, room_rate, is_occupied, status, guest_id)
VALUES ('F-102', 'FIRE', 150000, false, 'AVAILABLE', NULL)
ON DUPLICATE KEY UPDATE element_cap = VALUES(element_cap), room_rate = VALUES(room_rate);

INSERT INTO rooms (room_id, element_cap, room_rate, is_occupied, status, guest_id)
VALUES ('F-103', 'FIRE', 150000, false, 'AVAILABLE', NULL)
ON DUPLICATE KEY UPDATE element_cap = VALUES(element_cap), room_rate = VALUES(room_rate);

-- Water Habitat Rooms
INSERT INTO rooms (room_id, element_cap, room_rate, is_occupied, status, guest_id)
VALUES ('W-201', 'WATER', 170000, false, 'AVAILABLE', NULL)
ON DUPLICATE KEY UPDATE element_cap = VALUES(element_cap), room_rate = VALUES(room_rate);

INSERT INTO rooms (room_id, element_cap, room_rate, is_occupied, status, guest_id)
VALUES ('W-202', 'WATER', 170000, false, 'AVAILABLE', NULL)
ON DUPLICATE KEY UPDATE element_cap = VALUES(element_cap), room_rate = VALUES(room_rate);

INSERT INTO rooms (room_id, element_cap, room_rate, is_occupied, status, guest_id)
VALUES ('W-203', 'WATER', 170000, false, 'AVAILABLE', NULL)
ON DUPLICATE KEY UPDATE element_cap = VALUES(element_cap), room_rate = VALUES(room_rate);

-- Earth Habitat Rooms
INSERT INTO rooms (room_id, element_cap, room_rate, is_occupied, status, guest_id)
VALUES ('E-301', 'EARTH', 140000, false, 'AVAILABLE', NULL)
ON DUPLICATE KEY UPDATE element_cap = VALUES(element_cap), room_rate = VALUES(room_rate);

INSERT INTO rooms (room_id, element_cap, room_rate, is_occupied, status, guest_id)
VALUES ('E-302', 'EARTH', 140000, false, 'AVAILABLE', NULL)
ON DUPLICATE KEY UPDATE element_cap = VALUES(element_cap), room_rate = VALUES(room_rate);

INSERT INTO rooms (room_id, element_cap, room_rate, is_occupied, status, guest_id)
VALUES ('E-303', 'EARTH', 140000, false, 'AVAILABLE', NULL)
ON DUPLICATE KEY UPDATE element_cap = VALUES(element_cap), room_rate = VALUES(room_rate);


-- ==========================================
-- Master Data Menu Perawatan Spesifik (Services)
-- ==========================================

DELETE FROM services WHERE id IN (2, 4, 6);

INSERT INTO services (id, icon, price, service_name, target_element)
VALUES (1, 'fa-fire', 60000, 'Injeksi Magma', 'FIRE')
ON DUPLICATE KEY UPDATE icon = VALUES(icon), price = VALUES(price), service_name = VALUES(service_name), target_element = VALUES(target_element);

INSERT INTO services (id, icon, price, service_name, target_element)
VALUES (3, 'fa-droplet', 70000, 'Nutrisi Akuatik', 'WATER')
ON DUPLICATE KEY UPDATE icon = VALUES(icon), price = VALUES(price), service_name = VALUES(service_name), target_element = VALUES(target_element);

INSERT INTO services (id, icon, price, service_name, target_element)
VALUES (5, 'fa-leaf', 50000, 'Pupuk Mineral', 'EARTH')
ON DUPLICATE KEY UPDATE icon = VALUES(icon), price = VALUES(price), service_name = VALUES(service_name), target_element = VALUES(target_element);

-- Hash BCrypt berikut adalah representasi dari text "admin123"
INSERT INTO users (username, password, role)
VALUES ('admin', '$2a$10$KjdVekRNLv/pJwOo1P73oO.Z3dQfYSNNoFWutVvp/NJI0vVcVR5Sq', 'ADMIN')
ON DUPLICATE KEY UPDATE username = username;
