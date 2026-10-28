package org.instapay.controller;


import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.instapay.entity.Transaction;
import org.instapay.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transfer(@RequestParam String sender, @RequestParam String receiver, @RequestParam java.math.BigDecimal amount){
        return ResponseEntity.accepted().body(paymentService.transferPayment(sender,receiver,amount));
    }
}
