--=============================================
-- DATA SEEDER SCRIPT
----=============================================

-- ============================================
-- Monster Inn — Initial Seed Data
-- 9 Rooms: 3 Fire, 3 Water, 3 Earth
-- ============================================

-- Fire Habitat Rooms
INSERT INTO rooms (room_id, element_cap, room_rate, is_occupied, status, guest_id)
VALUES ('F-101', 'FIRE', 175000, false, 'AVAILABLE', NULL)
ON DUPLICATE KEY UPDATE room_id = room_id;

INSERT INTO rooms (room_id, element_cap, room_rate, is_occupied, status, guest_id)
VALUES ('F-102', 'FIRE', 175000, false, 'AVAILABLE', NULL)
ON DUPLICATE KEY UPDATE room_id = room_id;

INSERT INTO rooms (room_id, element_cap, room_rate, is_occupied, status, guest_id)
VALUES ('F-103', 'FIRE', 175000, false, 'AVAILABLE', NULL)
ON DUPLICATE KEY UPDATE room_id = room_id;

-- Water Habitat Rooms
INSERT INTO rooms (room_id, element_cap, room_rate, is_occupied, status, guest_id)
VALUES ('W-201', 'WATER', 190000, false, 'AVAILABLE', NULL)
ON DUPLICATE KEY UPDATE room_id = room_id;

INSERT INTO rooms (room_id, element_cap, room_rate, is_occupied, status, guest_id)
VALUES ('W-202', 'WATER', 190000, false, 'AVAILABLE', NULL)
ON DUPLICATE KEY UPDATE room_id = room_id;

INSERT INTO rooms (room_id, element_cap, room_rate, is_occupied, status, guest_id)
VALUES ('W-203', 'WATER', 190000, false, 'AVAILABLE', NULL)
ON DUPLICATE KEY UPDATE room_id = room_id;

-- Earth Habitat Rooms
INSERT INTO rooms (room_id, element_cap, room_rate, is_occupied, status, guest_id)
VALUES ('E-301', 'EARTH', 155000, false, 'AVAILABLE', NULL)
ON DUPLICATE KEY UPDATE room_id = room_id;

INSERT INTO rooms (room_id, element_cap, room_rate, is_occupied, status, guest_id)
VALUES ('E-302', 'EARTH', 155000, false, 'AVAILABLE', NULL)
ON DUPLICATE KEY UPDATE room_id = room_id;

INSERT INTO rooms (room_id, element_cap, room_rate, is_occupied, status, guest_id)
VALUES ('E-303', 'EARTH', 155000, false, 'AVAILABLE', NULL)
ON DUPLICATE KEY UPDATE room_id = room_id;


-- ==========================================
-- Master Data Menu Perawatan Spesifik (Services)
-- ==========================================

INSERT INTO services (id, icon, price, service_name, target_element)
VALUES (1, 'fa-fire', 60000, 'Magma Injection', 'FIRE')
ON DUPLICATE KEY UPDATE id = id;

INSERT INTO services (id, icon, price, service_name, target_element)
VALUES (2, 'fa-hot-tub-person', 75000, 'Lava Bath', 'FIRE')
ON DUPLICATE KEY UPDATE id = id;

INSERT INTO services (id, icon, price, service_name, target_element)
VALUES (3, 'fa-droplet', 70000, 'Aquatic Nutrient', 'WATER')
ON DUPLICATE KEY UPDATE id = id;

INSERT INTO services (id, icon, price, service_name, target_element)
VALUES (4, 'fa-cloud-showers-heavy', 45000, 'Deep Sea Mist', 'WATER')
ON DUPLICATE KEY UPDATE id = id;

INSERT INTO services (id, icon, price, service_name, target_element)
VALUES (5, 'fa-leaf', 50000, 'Soil Nutrient', 'EARTH')
ON DUPLICATE KEY UPDATE id = id;

INSERT INTO services (id, icon, price, service_name, target_element)
VALUES (6, 'fa-hand-holding-drop', 40000, 'Root Massage', 'EARTH')
ON DUPLICATE KEY UPDATE id = id;

-- Hash BCrypt berikut adalah representasi dari text "admin123"
INSERT INTO users (username, password, role)
VALUES ('admin', '$2a$10$KjdVekRNLv/pJwOo1P73oO.Z3dQfYSNNoFWutVvp/NJI0vVcVR5Sq', 'ADMIN')
ON DUPLICATE KEY UPDATE username = username;
