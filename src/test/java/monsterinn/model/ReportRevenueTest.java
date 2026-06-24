package monsterinn.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReportRevenueTest {

    @Test
    void overpaidPrepaidStillReportsGrossRevenue() {
        ReportManager reportManager = new ReportManager();
        Monster monster = new FireMonster("M20", "Overpaid", 175000, 0);
        monster.setStayDays(1);
        monster.setPrepaidAmount(200000);

        Room room = new Room("F20", "FIRE", 175000);
        room.checkIn(monster);

        Transaction transaction = new Transaction(monster, room, 0);
        assertTrue(transaction.processPayment());

        reportManager.addTransaction(transaction);

        assertEquals(175000, reportManager.getTotalRevenue());
        assertEquals(25000, transaction.getRefundAmount());
        assertEquals(0, transaction.getRemainingDue());
    }
}