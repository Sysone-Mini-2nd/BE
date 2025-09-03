package com.sys.stm.global.common.entity;

import lombok.Getter;

import java.sql.Timestamp;
import java.time.LocalDateTime;


@Getter
public class BaseEntity {

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private Boolean isDeleted = false;

    public void markAsDeleted() {
        this.isDeleted = true;
    }
}
