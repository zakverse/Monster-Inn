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