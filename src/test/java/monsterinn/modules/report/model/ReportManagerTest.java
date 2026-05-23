package monsterinn.modules.report.model;

import monsterinn.modules.monster.model.FireMonster;
import monsterinn.modules.monster.model.Monster;
import monsterinn.modules.room.model.Room;
import monsterinn.modules.room.model.RoomStatus;
import monsterinn.modules.transaction.model.Transaction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReportManagerTest {

    @Test
    void successfulTransactionIsAddedToReport() {
        ReportManager reportManager = new ReportManager();
        Monster monster = checkedInFireMonster("M01", "Salamander", "F01");
        Room room = occupiedFireRoom("F01", monster);

        Transaction transaction = new Transaction(monster, room, 200000);
        assertTrue(transaction.processPayment());

        reportManager.addTransaction(transaction);

        assertEquals(1, reportManager.getTransactions().size());
        assertEquals(transaction.getTotalCost(), reportManager.getTotalRevenue());
        assertEquals(1, reportManager.getTotalCheckedOutMonsters());
    }

    @Test
    void failedTransactionIsRejectedFromReport() {
        ReportManager reportManager = new ReportManager();
        Monster monster = checkedInFireMonster("M02", "Tiny Ember", "F02");
        Room room = occupiedFireRoom("F02", monster);

        Transaction transaction = new Transaction(monster, room, 1);
        assertFalse(transaction.processPayment());

        assertThrows(IllegalStateException.class, () -> reportManager.addTransaction(transaction));
        assertTrue(reportManager.getTransactions().isEmpty());
        assertEquals(0, reportManager.getTotalRevenue());
    }

    @Test
    void recentTransactionsReturnsNewestFirst() throws InterruptedException {
        ReportManager reportManager = new ReportManager();

        Transaction first = paidTransaction("M03", "First", "F03");
        Thread.sleep(2);
        Transaction second = paidTransaction("M04", "Second", "F04");

        reportManager.addTransaction(first);
        reportManager.addTransaction(second);

        assertEquals(second.getTransId(), reportManager.getRecentTransactions().get(0).getTransId());
        assertEquals(first.getTransId(), reportManager.getRecentTransactions().get(1).getTransId());
    }

    @Test
    void roomBecomesDirtyAfterSuccessfulCheckout() {
        Monster monster = checkedInFireMonster("M05", "Cinder", "F05");
        Room room = occupiedFireRoom("F05", monster);
        Transaction transaction = new Transaction(monster, room, 200000);

        assertTrue(transaction.processPayment());
        room.checkOut();

        assertFalse(room.isOccupied());
        assertNull(room.getCurrentGuest());
        assertEquals(RoomStatus.DIRTY, room.getStatus());
    }

    private Transaction paidTransaction(String monsterId, String name, String roomId) {
        Monster monster = checkedInFireMonster(monsterId, name, roomId);
        Room room = occupiedFireRoom(roomId, monster);
        Transaction transaction = new Transaction(monster, room, 200000);
        assertTrue(transaction.processPayment());
        return transaction;
    }

    private Monster checkedInFireMonster(String monsterId, String name, String roomId) {
        Monster monster = new FireMonster(monsterId, name, 150000, 25000);
        monster.setRoomId(roomId);
        monster.setStayDays(1);
        monster.setPrepaidAmount(0);
        return monster;
    }

    private Room occupiedFireRoom(String roomId, Monster monster) {
        Room room = new Room(roomId, "FIRE", 150000);
        room.checkIn(monster);
        return room;
    }
}
