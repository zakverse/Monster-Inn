package monsterinn.modules.transaction.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/checkout")
public class TransactionController {

    @GetMapping
    public String showCheckoutPage(Model model) {
        // Mengaktifkan menu 'Checkout' di sidebar
        model.addAttribute("activePage", "checkout");
        return "modules/transaction/checkout";
    }
}