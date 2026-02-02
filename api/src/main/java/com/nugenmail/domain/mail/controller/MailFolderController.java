package com.nugenmail.domain.mail.controller;

import com.nugenmail.common.ApiResponse;
import com.nugenmail.domain.mail.service.MailServiceFactory;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Mail Folder Controller
 * Manages folder operations including creation, deletion, renaming, moving,
 * backup, aging, and shared folder listing.
 */
@RestController
@RequestMapping("/api/mail/folders")
@RequiredArgsConstructor
public class MailFolderController {

    private final MailServiceFactory mailServiceFactory;

    /**
     * Creates a new folder.
     * 
     * @param name     Folder name
     * @param password User password
     */
    @PostMapping
    public ApiResponse<Void> createFolder(
            @RequestParam String name,
            @RequestParam String password) throws MessagingException {
        String userId = getCurrentUserId();
        mailServiceFactory.getService().createFolder(userId, password, name);
        return ApiResponse.success(null);
    }

    /**
     * Deletes a folder.
     * 
     * @param name     Folder name
     * @param password User password
     */
    @DeleteMapping
    public ApiResponse<Void> deleteFolder(
            @RequestParam String name,
            @RequestParam String password) throws MessagingException {
        String userId = getCurrentUserId();
        mailServiceFactory.getService().deleteFolder(userId, password, name);
        return ApiResponse.success(null);
    }

    /**
     * Renames a folder.
     * 
     * @param oldName  Old folder name
     * @param newName  New folder name
     * @param password User password
     */
    @PutMapping
    public ApiResponse<Void> renameFolder(
            @RequestParam String oldName,
            @RequestParam String newName,
            @RequestParam String password) throws MessagingException {
        String userId = getCurrentUserId();
        mailServiceFactory.getService().renameFolder(userId, password, oldName, newName);
        return ApiResponse.success(null);
    }

    /**
     * Moves a folder to a new parent.
     * 
     * @param name      Folder name
     * @param newParent New parent folder name
     * @param password  User password
     */
    @PutMapping("/move")
    public ApiResponse<Void> moveFolder(
            @RequestParam String name,
            @RequestParam String newParent,
            @RequestParam String password) throws MessagingException {
        String userId = getCurrentUserId();
        mailServiceFactory.getService().moveFolder(userId, password, name, newParent);
        return ApiResponse.success(null);
    }

    // --- Legacy Support Endpoints ---

    /**
     * Starts a backup for a folder.
     * 
     * @param name     Folder name
     * @param password User password
     */
    @PostMapping("/backup")
    public ApiResponse<Void> startBackup(
            @RequestParam String name,
            @RequestParam String password) throws MessagingException {
        String userId = getCurrentUserId();
        mailServiceFactory.getService().startFolderBackup(userId, password, name);
        return ApiResponse.success(null);
    }

    /**
     * Checks status of a backup job.
     * 
     * @param name     Folder name
     * @param password User password
     * @return Backup status JSON
     */
    @GetMapping("/backup/status")
    public ApiResponse<String> getBackupStatus(
            @RequestParam String name,
            @RequestParam String password) throws MessagingException {
        String userId = getCurrentUserId();
        return ApiResponse.success(mailServiceFactory.getService().getFolderBackupStatus(userId, password, name));
    }

    /**
     * Updates folder aging (expiration) days.
     * 
     * @param name     Folder name
     * @param days     Max age in days
     * @param password User password
     */
    @PutMapping("/aging")
    public ApiResponse<Void> updateAging(
            @RequestParam String name,
            @RequestParam int days,
            @RequestParam String password) throws MessagingException {
        String userId = getCurrentUserId();
        mailServiceFactory.getService().updateFolderAging(userId, password, name, days);
        return ApiResponse.success(null);
    }

    /**
     * Lists shared folders available to the user.
     * 
     * @param password User password
     * @return List of shared folder names
     */
    @GetMapping("/shared")
    public ApiResponse<List<String>> getSharedFolders(
            @RequestParam String password) throws MessagingException {
        String userId = getCurrentUserId();
        return ApiResponse.success(mailServiceFactory.getService().getSharedFolders(userId, password));
    }

    private String getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }
}
