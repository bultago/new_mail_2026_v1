package com.nugenmail.domain.mail.mapper;

import com.nugenmail.domain.mail.model.FolderPolicy;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * Folder Policy Mapper.
 * Manages folder retention (aging) policies in the database.
 */
@Mapper
public interface FolderPolicyMapper {

        /**
         * Finds policy for a specific user folder.
         */
        FolderPolicy findPolicy(@Param("userId") String userId, @Param("domain") String domain,
                        @Param("folderName") String folderName);

        /**
         * Inserts a new folder policy.
         */
        void insertPolicy(FolderPolicy policy);

        /**
         * Updates an existing folder policy.
         */
        void updatePolicy(FolderPolicy policy);

        /**
         * Updates system folder policy stored in user info table.
         */
        void updateSystemPolicy(@Param("userId") String userId, @Param("domain") String domain,
                        @Param("columnName") String columnName, @Param("keepDays") int keepDays);

        /**
         * Deletes a policy.
         */
        void deletePolicy(FolderPolicy policy);
}
