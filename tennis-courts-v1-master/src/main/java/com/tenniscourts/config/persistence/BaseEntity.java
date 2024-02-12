package com.tenniscourts.config.persistence;

import com.tenniscourts.audit.CustomAuditEntityListener;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(CustomAuditEntityListener.class)
public class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String ipNumberUpdate;

    @Column
    private Long userCreate;

    @Column
    private Long userUpdate;

    @Column
    @LastModifiedDate
    private LocalDateTime dateUpdate;

    @Column
    private String ipNumberCreate;

    @Column
    @CreatedDate
    private LocalDateTime dateCreate;

}
