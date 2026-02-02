package com.nugenmail.domain.mail.controller;

import com.nugenmail.common.ApiResponse;
import com.nugenmail.domain.mail.model.MailFolder;
import com.nugenmail.domain.mail.model.MailMessage;
import com.nugenmail.domain.mail.model.MailSendRequest;
import com.nugenmail.domain.mail.service.MailServiceFactory;
import com.nugenmail.domain.mail.service.Pop3Service;
import com.nugenmail.domain.mail.service.SmtpService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.zip.ZipOutputStream;
import com.nugenmail.common.util.DownloadUtil;

import java.util.List;

/**
 * Mail Controller
 * Exposes endpoints for accessing mail folders, messages, attachments, and
 * sending mail.
 */
@RestController
@RequestMapping("/api/mail")
@RequiredArgsConstructor
public class MailController {

    private final MailServiceFactory mailServiceFactory;
    private final SmtpService smtpService;
    private final Pop3Service pop3Service;

    /**
     * Retrieves the list of mail folders.
     * 
     * @param password User password (required for IMAP connection)
     * @return List of MailFolders
     * @throws MessagingException if mail access fails
     */
    @GetMapping("/folders")
    public ApiResponse<List<MailFolder>> getFolders(@RequestParam String password) throws MessagingException {
        // In a real app, password should not be a query param.
        // For MVP without stateful session, we accept it temporarily or use a master
        // password.
        String userId = getCurrentUserId();
        return ApiResponse.success(mailServiceFactory.getService().getFolderList(userId, password));
    }

    /**
     * Retrieves a list of messages from a specific folder.
     * 
     * @param password User password
     * @param protocol Protocol (imap vs pop3)
     * @param folder   Folder name (default INBOX)
     * @return List of MailMessage items
     * @throws MessagingException
     */
    @GetMapping("/messages")
    public ApiResponse<List<MailMessage>> getMessages(
            @RequestParam String password,
            @RequestParam(defaultValue = "imap") String protocol,
            @RequestParam(required = false, defaultValue = "INBOX") String folder)
            throws MessagingException {
        String userId = getCurrentUserId();
        if ("pop3".equalsIgnoreCase(protocol)) {
            return ApiResponse.success(pop3Service.getMessages(userId, password));
        }
        return ApiResponse.success(mailServiceFactory.getService().getMessages(userId, password, folder));
    }

    /**
     * Retrieves details of a specific message.
     * 
     * @param uid      Message UID
     * @param password User password
     * @param folder   Folder name
     * @return MailDetail object
     * @throws MessagingException
     * @throws java.io.IOException
     */
    @GetMapping("/messages/{uid}")
    public ApiResponse<com.nugenmail.domain.mail.model.MailDetail> getMessageDetail(
            @PathVariable long uid,
            @RequestParam String password,
            @RequestParam(required = false, defaultValue = "INBOX") String folder)
            throws MessagingException, java.io.IOException {
        String userId = getCurrentUserId();
        return ApiResponse.success(mailServiceFactory.getService().getMessageDetail(userId, password, folder, uid));
    }

    /**
     * Downloads a specific attachment.
     * 
     * @param uid      Message UID
     * @param partId   Attachment BodyPart ID/Index
     * @param password User Password
     * @param folder   Folder Name
     * @param response HttpServletResponse to write stream
     * @throws MessagingException
     * @throws IOException
     */
    @GetMapping("/messages/{uid}/attachments/{partId}")
    public void downloadAttachment(
            @PathVariable long uid,
            @PathVariable String partId,
            @RequestParam String password,
            @RequestParam(required = false, defaultValue = "INBOX") String folder,
            HttpServletResponse response) throws MessagingException, IOException {
        String userId = getCurrentUserId();
        // Filename is not easily available here without fetching structure again.
        // Frontend should perhaps provide it or we set a default.
        response.setHeader("Content-Disposition", "attachment; filename=\"attachment_" + partId + ".dat\"");
        mailServiceFactory.getService().downloadAttachment(userId, password, folder, uid, partId,
                response.getOutputStream());
    }

    /**
     * Sends an email.
     * 
     * @param request MailSendRequest containing recipient, subject, body
     * @return Success response
     * @throws MessagingException
     */
    @PostMapping("/send")
    public ApiResponse<Void> sendMail(@RequestBody MailSendRequest request) throws MessagingException {
        String userId = getCurrentUserId();
        smtpService.sendMessage(userId, request.getPassword(), request);
        return ApiResponse.success(null);
    }

    /**
     * Downloads all attachments as a ZIP file.
     * 
     * @param uid      Message UID
     * @param password User Password
     * @param folder   Folder Name
     * @param zipName  Name of the resulting ZIP file
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @throws MessagingException
     * @throws IOException
     */
    @GetMapping("/messages/{uid}/attachments")
    public void downloadAllAttachments(
            @PathVariable long uid,
            @RequestParam String password,
            @RequestParam(required = false, defaultValue = "INBOX") String folder,
            @RequestParam(required = false, defaultValue = "attachments.zip") String zipName,
            HttpServletRequest request,
            HttpServletResponse response) throws MessagingException, IOException {

        String userId = getCurrentUserId();
        // Use DownloadUtil if available, else generic
        String encodedName = zipName;
        try {
            encodedName = DownloadUtil.getDownloadFileName(zipName, request.getHeader("User-Agent"));
        } catch (Exception e) {
            // fallback
        }

        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedName + "\"");

        try (ZipOutputStream zos = new ZipOutputStream(response.getOutputStream())) {
            mailServiceFactory.getService().downloadAllAttachments(userId, password, folder, uid, zos);
        }
    }

    private String getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }
}
