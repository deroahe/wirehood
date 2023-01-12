package com.wirehood.adminservice.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity<T> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private T id;

    @Column(name = "created_date")
    @Temporal(value = TemporalType.DATE)
    private Date createdDate;

    @Column(name = "last_modified_date")
    @Temporal(value = TemporalType.DATE)
    private Date lastModifiedDate;
}
