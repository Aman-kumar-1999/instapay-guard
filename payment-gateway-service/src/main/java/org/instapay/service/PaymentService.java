package org.instapay.service;

import lombok.AllArgsConstructor;
import org.instapay.config.KafkaTopicConfig;
import org.instapay.dto.PaymentEvent;
import org.instapay.entity.Transaction;
import org.instapay.repo.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentService {

    @Autowired
    private TransactionRepo transactionRepo;
    @Autowired
    private KafkaTemplate<String, PaymentEvent> kafkaTemplate;


    public Transaction transferPayment(String sender, String receiver, BigDecimal amount){
        Transaction transaction = Transaction.builder()
                .senderAccount(sender)
                .receiverAccount(receiver)
                .amount(amount)
                .status("PENDING")
                .build();
        transactionRepo.save(transaction);
        PaymentEvent event = PaymentEvent.builder()
                .transactionId(transaction.getTransactionId())
                .senderAccount(transaction.getSenderAccount())
                .receiverAccount(transaction.getReceiverAccount())
                .amount(amount)
                .status("PENDING")
                .timestamp(transaction.getTimestamp())
                .build();
        kafkaTemplate.send(KafkaTopicConfig.PAYMENT_TOPIC, transaction.getTransactionId(), event);

        return transaction;
    }

}
