package com.example.eunboard.domain.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
@Getter
public class BaseEntity {

    @CreatedDate
    @Column(name="create_date",updatable=false)
    private LocalDateTime createDate; //작성일

    @LastModifiedDate
    @Column(name="update_date")
    private LocalDateTime updateDate; //수정일

}
