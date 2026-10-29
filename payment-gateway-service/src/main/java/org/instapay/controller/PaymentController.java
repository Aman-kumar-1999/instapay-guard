package org.instapay.controller;

import org.instapay.entity.Transaction;
import org.instapay.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService; // Must be final for @RequiredArgsConstructor

    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transfer(@RequestParam String sender,
                                                @RequestParam String receiver,
                                                @RequestParam java.math.BigDecimal amount) {
        return ResponseEntity.accepted().body(paymentService.transferPayment(sender, receiver, amount));
    }
}