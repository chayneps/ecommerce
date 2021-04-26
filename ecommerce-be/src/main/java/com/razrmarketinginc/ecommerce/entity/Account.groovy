package com.razrmarketinginc.ecommerce.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Index
import javax.persistence.OneToMany
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Entity
class Account extends ENTITY{

    @Id
    String id

    @Column(unique=true)
    Integer accountNumber

    @OneToMany(mappedBy="account")
    Set<Transaction> transactions

}

