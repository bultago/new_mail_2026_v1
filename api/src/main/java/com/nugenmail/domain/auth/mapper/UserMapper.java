package com.nugenmail.domain.auth.mapper;

import com.nugenmail.domain.auth.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * User MyBatis Mapper
 * Accesses legacy mail_user and mail_domain tables.
 */
@Mapper
public interface UserMapper {

    /**
     * Finds a user by ID and Domain.
     * 
     * @param userId User ID (mail_uid)
     * @param domain Domain name (mail_domain)
     * @return User object or null if not found
     */
    User findByUserIdAndDomain(@Param("userId") String userId, @Param("domain") String domain);
}
