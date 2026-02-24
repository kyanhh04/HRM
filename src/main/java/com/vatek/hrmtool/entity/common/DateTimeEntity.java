package com.vatek.hrmtool.entity.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@MappedSuperclass
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class    DateTimeEntity {
    @Column(nullable = false, updatable = false)
    @CreatedDate
    private Instant createdTime;

    @Column(nullable = false, updatable = false)
    @CreatedBy
    private String createdBy;

    @LastModifiedDate
    private Instant modifiedTime;

    @LastModifiedBy
    private String modifiedBy;
}
