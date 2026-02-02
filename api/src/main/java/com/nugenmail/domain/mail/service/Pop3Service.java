package com.nugenmail.domain.mail.service;

import com.nugenmail.domain.mail.model.MailMessage;
import jakarta.mail.FetchProfile;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Store;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * POP3 Service
 * Handles fetching messages via POP3 protocol.
 * Currently limited to INBOX retrieval.
 */
@Service
public class Pop3Service {

    @Value("${spring.mail.host:localhost}")
    private String mailHost;

    private Session createSession() {
        Properties props = new Properties();
        props.put("mail.store.protocol", "pop3");
        props.put("mail.pop3.host", mailHost);
        return Session.getInstance(props);
    }

    /**
     * Retrieves messages from POP3 INBOX.
     * Fetches top 20 recent messages.
     * 
     * @param username POP3 Username
     * @param password POP3 Password
     * @return List of MailMessage objects
     * @throws MessagingException if connection or fetch fails
     */
    public List<MailMessage> getMessages(String username, String password) throws MessagingException {
        Session session = createSession();
        Store store = session.getStore("pop3");
        store.connect(username, password);

        // POP3 only supports INBOX
        Folder folder = store.getFolder("INBOX");
        if (!folder.exists()) {
            store.close();
            throw new MessagingException("INBOX not found");
        }

        folder.open(Folder.READ_ONLY);

        // Fetch last 20 messages for MVP
        int messageCount = folder.getMessageCount();
        int start = Math.max(1, messageCount - 19);
        int end = messageCount;

        List<MailMessage> mailMessages = new ArrayList<>();
        if (messageCount > 0) {
            Message[] messages = folder.getMessages(start, end);

            // Fetch envelope for performance
            FetchProfile fp = new FetchProfile();
            fp.add(FetchProfile.Item.ENVELOPE);
            folder.fetch(messages, fp);

            for (int i = messages.length - 1; i >= 0; i--) { // Reverse order
                Message msg = messages[i];
                mailMessages.add(MailMessage.builder()
                        .uid(i) // UID in POP3 is string, using index for MVP list
                        .subject(msg.getSubject())
                        .from(msg.getFrom()[0].toString())
                        .sentDate(msg.getSentDate())
                        // POP3 doesn't support flags like SEEN in the same way as IMAP server-side
                        .seen(false)
                        .build());
            }
        }

        folder.close(false);
        store.close();
        return mailMessages;
    }
}
