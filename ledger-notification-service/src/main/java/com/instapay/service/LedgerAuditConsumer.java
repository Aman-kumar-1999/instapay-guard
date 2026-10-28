package com.instapay.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service @Slf4j
public class LedgerAuditConsumer {

    @KafkaListener(topics = "bank.payments.initiated", groupId = "ledger-group")
    public void recordLedger(PaymentEvent event) {
        log.info("Ledger Audit Log recorded for Transaction ID: {} | Amount: {}", event.getTransactionId(), event.getAmount());


    }

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class PaymentEvent {
        private String transactionId;
        private String senderAccount;
        private String receiverAccount;
        private BigDecimal amount;
        private LocalDateTime timestamp;
        private String status;
    }
}
