package monsterinn.modules.transaction.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/transaction")
public class TransactionController {

    @GetMapping("/checkout")
    public String showCheckoutPage() {
        // Asumsi UI HTML bernama checkout.html sudah disiapkan oleh Front-end
        return "modules/dashboard/checkout"; 
    }

    @PostMapping("/process")
    public String processCheckout(
            @RequestParam("transId") String transId,
            @RequestParam("roomId") String roomId,
            @RequestParam("guestId") String guestId,
            @RequestParam("durationDays") Integer durationDays,
            @RequestParam("cashPaid") Double cashPaid,
            Model model) {

        try {
            // Di tahap selanjutnya, kita akan memanggil Database Repository di sini
            System.out.println("Memproses pembayaran untuk Transaksi: " + transId);
            
            // Redirect ke halaman sukses / dashboard
            return "redirect:/dashboard";

        } catch (Exception e) {
            model.addAttribute("error", "Terjadi kesalahan saat memproses pembayaran.");
            return "modules/dashboard/checkout";
        }
    }
}