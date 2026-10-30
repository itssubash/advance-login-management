package com.yenyasoft.admin_user_management.service.listeners;

import com.yenyasoft.admin_user_management.events.AccountApprovedEvent;
import com.yenyasoft.admin_user_management.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountActivationEmailListener {
    private final EmailService emailService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendActivationEmail(AccountApprovedEvent event) {
        try {
            emailService.sendActivationMail(event.email());
        } catch (RuntimeException exception) {
            log.error("Failed to send account activation email to {}", event.email(), exception);
        }
    }
}
