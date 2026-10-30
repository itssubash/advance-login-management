package com.yenyasoft.admin_user_management.service.listeners;

import com.yenyasoft.admin_user_management.events.AccountRejectedEvent;
import com.yenyasoft.admin_user_management.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Author: Subash
 * Created: 10/30/2026
 * File: AccountRejectionListener.java
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class AccountRejectionListener {
    private final EmailService emailService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendRejectionMail(AccountRejectedEvent event){
        try {
            emailService.sendRejectedMail(event.email());
        }catch (RuntimeException exception){
            log.error("Failed to send account rejection email to {}", event.email(), exception);
        }
    }
}
