package com.nugenmail.domain.mail.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Mail Service Factory
 * Selects the appropriate MailServiceProvider based on configuration.
 */
@Service
@RequiredArgsConstructor
public class MailServiceFactory {

    private final StandardImapService standardImapService;
    private final TerraceImapService terraceImapService;

    @Value("${nugenmail.provider:standard}")
    private String providerType;

    /**
     * Returns the configured mail service provider.
     * 
     * @return MailServiceProvider implementation
     */
    public MailServiceProvider getService() {
        if ("legacy".equalsIgnoreCase(providerType)) {
            return terraceImapService;
        }
        return standardImapService;
    }
}
