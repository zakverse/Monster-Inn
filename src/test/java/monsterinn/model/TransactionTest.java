package monsterinn.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    @Test
    void prepaidLessThanTotalRequiresRemainingDue() {
        Monster monster = checkedInFireMonster("M10", "Ember", "F10", 200000, 50000, 200000);
        Room room = occupiedFireRoom("F10", monster, 200000);

        Transaction transaction = new Transaction(monster, room, 50000);

        assertEquals(250000, transaction.getTotalCost());
        assertEquals(200000, transaction.getPrepaidAmount());
        assertEquals(50000, transaction.getRemainingDue());
        assertEquals(0, transaction.getRefundAmount());
        assertTrue(transaction.processPayment());
    }

    @Test
    void prepaidEqualTotalRequiresNoAdditionalPayment() {
        Monster monster = checkedInFireMonster("M11", "Ash", "F11", 200000, 0, 200000);
        Room room = occupiedFireRoom("F11", monster, 200000);

        Transaction transaction = new Transaction(monster, room, 0);

        assertEquals(200000, transaction.getTotalCost());
        assertEquals(0, transaction.getRemainingDue());
        assertEquals(0, transaction.getRefundAmount());
        assertTrue(transaction.processPayment());
    }

    @Test
    void prepaidGreaterThanTotalCreatesRefundWithoutNegativeDue() {
        Monster monster = checkedInFireMonster("M12", "Cinder", "F12", 175000, 0, 200000);
        Room room = occupiedFireRoom("F12", monster, 175000);

        Transaction transaction = new Transaction(monster, room, 0);

        assertEquals(175000, transaction.getTotalCost());
        assertEquals(0, transaction.getRemainingDue());
        assertEquals(25000, transaction.getRefundAmount());
        assertEquals(0, transaction.getPaymentAmount());
        assertTrue(transaction.processPayment());
    }

    @Test
    void negativePaymentIsNormalizedToZero() {
        Monster monster = checkedInFireMonster("M13", "Flare", "F13", 250000, 0, 200000);
        Room room = occupiedFireRoom("F13", monster, 250000);

        Transaction transaction = new Transaction(monster, room, -25000);

        assertEquals(0, transaction.getPaymentAmount());
        assertEquals(50000, transaction.getRemainingDue());
        assertFalse(transaction.processPayment());
    }

    @Test
    void prepaidGreaterThanTotalIsPaidSuccessfully() {
        Monster monster = checkedInFireMonster("M14", "Blaze", "F14", 175000, 0, 200000);
        Room room = occupiedFireRoom("F14", monster, 175000);

        Transaction transaction = new Transaction(monster, room, 0);

        assertTrue(transaction.isPaid());
        assertTrue(transaction.processPayment());
    }

    @Test
    void testTotalKamarPlusLayananMinusDeposit() {
        // Room rate: 150000, surcharge: 25000 -> 175000 per day.
        // stayDays: 3 days -> Room total = 175000 * 3 = 525000.
        // serviceTotal (Layanan): 60000.
        // Deposit (Prepaid): 200000.
        // Expected gross total cost = 525000 + 60000 = 585000.
        // Remaining due (Sisa Bayar) = 585000 - 200000 = 385000.
        
        Monster monster = new FireMonster("M99", "Pyro", 150000, 25000);
        monster.setRoomId("F-99");
        monster.setStayDays(3);
        monster.setPrepaidAmount(200000);
        monster.pushService("Injeksi Magma", 60000);

        Room room = new Room("F-99", "FIRE", 150000);
        room.checkIn(monster);

        // Kasir melunasi dengan input sisa bayar (385000)
        Transaction transaction = new Transaction(monster, room, 385000);

        assertEquals(585000, transaction.getTotalCost());
        assertEquals(200000, transaction.getPrepaidAmount());
        assertEquals(385000, transaction.getRemainingDue());
        assertEquals(0, transaction.getRefundAmount());
        assertEquals(385000, transaction.getPaymentAmount());
        assertEquals(0, transaction.getChangeAmount());
        assertTrue(transaction.processPayment());
    }

    private Monster checkedInFireMonster(String monsterId, String name, String roomId, double roomRate, double serviceTotal, double prepaid) {
        Monster monster = new FireMonster(monsterId, name, roomRate, 0);
        monster.setRoomId(roomId);
        monster.setStayDays(1);
        monster.setPrepaidAmount(prepaid);
        if (serviceTotal > 0) {
            monster.pushService("Layanan Tambahan", serviceTotal);
        }
        return monster;
    }

    private Room occupiedFireRoom(String roomId, Monster monster, double roomRate) {
        Room room = new Room(roomId, "FIRE", roomRate);
        room.checkIn(monster);
        return room;
    }
}