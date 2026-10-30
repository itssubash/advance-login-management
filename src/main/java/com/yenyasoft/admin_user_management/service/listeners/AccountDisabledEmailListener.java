package com.yenyasoft.admin_user_management.service.listeners;

import com.yenyasoft.admin_user_management.events.AccountDisabledEvent;
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
 * File: AccountDisabledEmailListener.java
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class AccountDisabledEmailListener {
    private final EmailService emailService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)

    public void sendAccountDisabledMail(AccountDisabledEvent event){
        try {
            emailService.sendDisabledMail(event.email());
        }catch (RuntimeException e){
            log.error("Failed to send account disabled email to {}", event.email(), e);
        }
    }
}
