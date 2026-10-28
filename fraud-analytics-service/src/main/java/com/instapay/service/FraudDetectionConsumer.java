package com.instapay.service;

//import com.instapay.dto.PaymentEvent;
import com.instapay.dto.PaymentEvent;
import com.instapay.dto.Transaction;
import com.instapay.repo.PaymentRepo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FraudDetectionConsumer {

    private final PaymentRepo paymentRepo;

    @KafkaListener(topics = "bank.payments.initiated", groupId = "fraud-group")
    public void consumePaymentEvent(PaymentEvent paymentEvent) {
        // Process the payment event
        paymentRepo.findById(paymentEvent.getTransactionId()).ifPresentOrElse(transaction -> {
            transaction.setStatus("APPROVED");
            paymentRepo.save(transaction);
            System.out.println("Transaction approved: " + transaction);
        }, () -> {
            // Handle the case where the transaction is not found
            Transaction transaction = new Transaction();
            transaction.setTransactionId(paymentEvent.getTransactionId());
            transaction.setSenderAccount(paymentEvent.getSenderAccount());
            transaction.setReceiverAccount(paymentEvent.getReceiverAccount());
            transaction.setAmount(paymentEvent.getAmount());
            transaction.setStatus("REJECTED");
            paymentRepo.save(transaction);
            throw new RuntimeException("Transaction not found");
        });
    }


}
