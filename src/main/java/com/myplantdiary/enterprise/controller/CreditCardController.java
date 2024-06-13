package com.myplantdiary.enterprise.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CreditCardController {

//    @GetMapping("/cardpayment")
//    public String showCheckForm() {
//        return "cardpayment";
//    }
//
//    @PostMapping("/cardpayment")
//    public String checkCreditCard(@RequestParam("creditCardNumber") String creditCardNumber, Model model) {
//        boolean isValid = isValidCreditCard(creditCardNumber);
//        model.addAttribute("isValid", isValid);
//        return "cardpayment";
//    }

    private boolean isValidCreditCard(String creditCardNumber) {
        // Implement Luhn algorithm to check credit card validity
        // You can find various implementations online
        // Here is a simple example:
        int sum = 0;
        boolean alternate = false;
        for (int i = creditCardNumber.length() - 1; i >= 0; i--) {
            int digit = Integer.parseInt(creditCardNumber.substring(i, i + 1));
            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            sum += digit;
            alternate = !alternate;
        }
        return sum % 10 == 0;
    }
}
