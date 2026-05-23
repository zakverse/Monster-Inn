package monsterinn.modules.transaction.model;

import monsterinn.modules.monster.model.Monster;
import monsterinn.modules.room.model.Room;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Transaction {
    private final String transId;
    private final String guestId;
    private final String guestName;
    private final String element;
    private final String roomId;
    private final int stayDays;
    private final double roomTotal;
    private final double serviceTotal;
    private final double prepaidAmount;
    private final double totalCost;
    private final double paymentAmount;
    private final double changeAmount;
    private final LocalDateTime checkoutTime;
    private final List<String> serviceLog;
    private boolean paid;

    public Transaction(Monster monster, Room room, double paymentAmount) {
        if (monster == null) {
            throw new IllegalArgumentException("Monster tidak boleh kosong");
        }
        if (room == null) {
            throw new IllegalArgumentException("Room tidak boleh kosong");
        }

        this.transId = "TX-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.guestId = monster.getIdMonster();
        this.guestName = monster.getName();
        this.element = monster.getElement();
        this.roomId = room.getRoomId();
        this.stayDays = monster.getStayDays();
        this.serviceTotal = monster.getExtraCost();
        this.roomTotal = monster.calculateTotalCost() - monster.getExtraCost();
        this.prepaidAmount = monster.getPrepaidAmount();
        this.totalCost = Math.max(0, monster.calculateTotalCost() - monster.getPrepaidAmount());
        this.paymentAmount = paymentAmount;
        this.changeAmount = Math.max(0, paymentAmount - this.totalCost);
        this.checkoutTime = LocalDateTime.now();
        this.serviceLog = new ArrayList<>(monster.getServiceLog());
    }

    public double calculateTotal() {
        return totalCost;
    }

    public boolean processPayment() {
        this.paid = paymentAmount >= totalCost;
        return paid;
    }

    public String getTransId() {
        return transId;
    }

    public String getGuestId() {
        return guestId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getName() {
        return guestName;
    }

    public String getElement() {
        return element;
    }

    public String getRoomId() {
        return roomId;
    }

    public int getStayDays() {
        return stayDays;
    }

    public double getRoomTotal() {
        return roomTotal;
    }

    public double getServiceTotal() {
        return serviceTotal;
    }

    public double getPrepaidAmount() {
        return prepaidAmount;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public double calculateTotalCost() {
        return totalCost;
    }

    public double getPaymentAmount() {
        return paymentAmount;
    }

    public double getChangeAmount() {
        return changeAmount;
    }

    public LocalDateTime getCheckoutTime() {
        return checkoutTime;
    }

    public List<String> getServiceLog() {
        return List.copyOf(serviceLog);
    }

    public boolean isPaid() {
        return paid;
    }
}
