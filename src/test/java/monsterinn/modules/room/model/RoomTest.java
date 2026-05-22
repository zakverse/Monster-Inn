package monsterinn.modules.room.model;

import monsterinn.modules.monster.model.EarthMonster;
import monsterinn.modules.monster.model.FireMonster;
import monsterinn.modules.monster.model.Monster;
import monsterinn.modules.monster.model.WaterMonster;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RoomTest {

    @Test
    public void testCheckInFireMonster() {
        Room room = new Room("R01", "FIRE", 10000);
        Monster fireMonster = new FireMonster("M01", "Charmander", 5000, 1000);
        room.checkIn(fireMonster);
        
        assertTrue(room.isOccupied());
        assertEquals(RoomStatus.OCCUPIED, room.getStatus());
        assertEquals(fireMonster, room.getCurrentGuest());
    }

    @Test
    public void testCheckInWaterMonster() {
        Room room = new Room("R02", "WATER", 12000);
        Monster waterMonster = new WaterMonster("M02", "Squirtle", 6000, 1500);
        room.checkIn(waterMonster);
        
        assertTrue(room.isOccupied());
        assertEquals(RoomStatus.OCCUPIED, room.getStatus());
        assertEquals(waterMonster, room.getCurrentGuest());
    }

    @Test
    public void testCheckInEarthMonster() {
        Room room = new Room("R03", "EARTH", 11000);
        Monster earthMonster = new EarthMonster("M03", "Bulbasaur", 5500, 1200);
        room.checkIn(earthMonster);
        
        assertTrue(room.isOccupied());
        assertEquals(RoomStatus.OCCUPIED, room.getStatus());
        assertEquals(earthMonster, room.getCurrentGuest());
    }

    @Test
    public void testCheckInMismatchedElementFails() {
        Room room = new Room("R01", "FIRE", 10000);
        Monster waterMonster = new WaterMonster("M02", "Squirtle", 6000, 1500);
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            room.checkIn(waterMonster);
        });
        
        assertTrue(exception.getMessage().contains("tidak cocok dengan habitat"));
        assertFalse(room.isOccupied());
        assertEquals(RoomStatus.AVAILABLE, room.getStatus());
        assertNull(room.getCurrentGuest());
    }

    @Test
    public void testCheckInNonAvailableRoomFails() {
        Room room = new Room("R01", "FIRE", 10000);
        room.setStatus(RoomStatus.DIRTY);
        Monster fireMonster = new FireMonster("M01", "Charmander", 5000, 1000);
        
        assertThrows(IllegalStateException.class, () -> {
            room.checkIn(fireMonster);
        });
        
        assertFalse(room.isOccupied());
        assertNull(room.getCurrentGuest());
    }

    @Test
    public void testCheckOutOccupiedRoom() {
        Room room = new Room("R01", "FIRE", 10000);
        Monster fireMonster = new FireMonster("M01", "Charmander", 5000, 1000);
        room.checkIn(fireMonster);
        
        room.checkOut();
        
        assertFalse(room.isOccupied());
        assertNull(room.getCurrentGuest());
        assertEquals(RoomStatus.DIRTY, room.getStatus());
    }

    @Test
    public void testCheckOutNonOccupiedRoomFails() {
        Room room = new Room("R01", "FIRE", 10000);
        
        assertThrows(IllegalStateException.class, () -> {
            room.checkOut();
        });
    }
}
