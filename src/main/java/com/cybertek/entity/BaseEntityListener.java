package com.cybertek.entity;

import com.cybertek.entity.common.UserPrincipal;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

// because we cannot inject anything to entity.. we created this listener to override prepresist and preupdate methods
@Component
public class BaseEntityListener extends AuditingEntityListener {

    @PrePersist
    private void onPrePersist(BaseEntity baseEntity){

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        baseEntity.insertDateTime = LocalDateTime.now();
        baseEntity.lastUpdateDateTime = LocalDateTime.now();
        baseEntity.insertUserId = 1L;
        baseEntity.lastUpdateUserId = 1L;

        if (authentication != null && !authentication.getName().equals("anonymousUser")){
            Object principal = authentication.getPrincipal();
            baseEntity.insertUserId = ((UserPrincipal) principal).getId();
            baseEntity.lastUpdateUserId = ((UserPrincipal) principal).getId();
        }
    }

    @PreUpdate
    private void onPreUpdate(BaseEntity baseEntity){

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        baseEntity.lastUpdateDateTime = LocalDateTime.now();
        baseEntity.lastUpdateUserId = 1L;
        if (authentication != null && !authentication.getName().equals("anonymousUser")){
            Object principal = authentication.getPrincipal();
            baseEntity.lastUpdateUserId = ((UserPrincipal) principal).getId();
        }

    }
}
