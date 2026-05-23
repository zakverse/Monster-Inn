package monsterinn.modules.transaction.controller;

import monsterinn.modules.monster.model.Monster;
import monsterinn.modules.monster.repository.MonsterRepository;
import monsterinn.modules.report.model.ReportManager;
import monsterinn.modules.room.model.Room;
import monsterinn.modules.room.repository.RoomRepository;
import monsterinn.modules.service.repository.ServiceRepository;
import monsterinn.modules.transaction.model.Transaction;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CheckoutController {

    @Autowired private MonsterRepository monsterRepository;
    @Autowired private RoomRepository roomRepository;
    @Autowired private ServiceRepository serviceRepository;
    @Autowired private ReportManager reportManager;

    @GetMapping("/checkout")
    public String showCheckout(Model model) {
        List<Monster> activeGuests = monsterRepository.findByRoomIdIsNotNull();
        model.addAttribute("activeGuests", activeGuests);
        return "modules/transaction/checkout";
    }

    @GetMapping("/api/checkout-info/{guestId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getCheckoutInfo(@PathVariable String guestId) {
        return monsterRepository.findById(guestId).map(monster -> {
            Map<String, Object> info = new HashMap<>();
            info.put("name",       monster.getName());
            info.put("roomId",     monster.getRoomId());
            info.put("stayDays",   monster.getStayDays());
            info.put("roomTotal",  monster.calculateTotalCost() - monster.getExtraCost());
            info.put("extraCost",  monster.getExtraCost());
            info.put("prepaid",    monster.getPrepaidAmount());
            info.put("grandTotal", Math.max(0, monster.calculateTotalCost() - monster.getPrepaidAmount()));
            info.put("serviceLog", monster.getServiceLog());
            return ResponseEntity.ok(info);
        }).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/api/checkout/confirm/{guestId}")
    @ResponseBody
    @Transactional
    public ResponseEntity<Map<String, Object>> confirmCheckout(
            @PathVariable String guestId,
            @RequestBody(required = false) Map<String, Object> body) {
        try {
            Monster monster = monsterRepository.findById(guestId)
                .orElseThrow(() -> new IllegalArgumentException("Monster tidak ditemukan: " + guestId));

            String roomId = monster.getRoomId();
            if (roomId == null || roomId.isBlank()) {
                throw new IllegalStateException("Monster belum menempati kamar");
            }

            Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Kamar tidak ditemukan: " + roomId));

            double totalDue = Math.max(0, monster.calculateTotalCost() - monster.getPrepaidAmount());
            double paymentAmount = readPaymentAmount(body, totalDue);

            Transaction transaction = new Transaction(monster, room, paymentAmount);
            transaction.calculateTotal();

            if (!transaction.processPayment()) {
                Map<String, Object> failed = new HashMap<>();
                failed.put("success", false);
                failed.put("message", "Pembayaran kurang. Total tagihan Rp " + totalDue);
                failed.put("totalDue", totalDue);
                failed.put("paymentAmount", paymentAmount);
                return ResponseEntity.badRequest().body(failed);
            }

            room.checkOut();
            roomRepository.save(room);

            // Hapus semua service log monster ini
            serviceRepository.findByGuestId(guestId).forEach(sr -> serviceRepository.delete(sr));

            // Hapus monster dari DB
            monsterRepository.delete(monster);

            reportManager.addTransaction(transaction);

            Map<String, Object> success = new HashMap<>();
            success.put("success", true);
            success.put("message", "Checkout berhasil");
            success.put("transactionId", transaction.getTransId());
            success.put("totalCost", transaction.getTotalCost());
            success.put("paymentAmount", transaction.getPaymentAmount());
            success.put("changeAmount", transaction.getChangeAmount());
            return ResponseEntity.ok(success);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    private double readPaymentAmount(Map<String, Object> body, double fallbackAmount) {
        if (body == null || !body.containsKey("paymentAmount")) {
            return fallbackAmount;
        }

        Object rawPayment = body.get("paymentAmount");
        if (rawPayment instanceof Number number) {
            return number.doubleValue();
        }

        String paymentText = String.valueOf(rawPayment).trim();
        if (paymentText.isEmpty()) {
            return fallbackAmount;
        }

        return Double.parseDouble(paymentText);
    }
}
