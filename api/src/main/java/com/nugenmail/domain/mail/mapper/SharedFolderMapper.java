package com.nugenmail.domain.mail.mapper;

import com.nugenmail.domain.mail.model.SharedFolder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

/**
 * Shared Folder Mapper.
 * Retrieves shared folder information from the database.
 */
@Mapper
public interface SharedFolderMapper {

    /**
     * Finds shared folders accessible by a user.
     * 
     * @param userId User ID
     * @param domain Domain name
     * @return List of SharedFolder objects
     */
    List<SharedFolder> findSharedFolders(@Param("userId") String userId, @Param("domain") String domain);
}
