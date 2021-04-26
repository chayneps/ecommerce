package com.razrmarketinginc.ecommerce.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.sql.Timestamp;

@Setter
@Getter
@MappedSuperclass
public class ENTITY {

    public static final String ACTIVE="A";
    public static final String PENDING="P";
    public static final String DELETE="D";

    @Column
    private Timestamp created;
    @Column
    private Timestamp lastModified;
    @Column(length=64)
    private String status;

}
