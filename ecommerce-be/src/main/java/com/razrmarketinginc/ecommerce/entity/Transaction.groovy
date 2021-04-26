package com.razrmarketinginc.ecommerce.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Index
import javax.persistence.Lob
import javax.persistence.ManyToOne
import javax.persistence.OneToOne
import javax.persistence.Table
import javax.persistence.Version

@Entity
@Table(indexes = [
        @Index(columnList = "type", name = "transaction_type_idx"),
        @Index(columnList = "created", name="transaction_created_index")
])
class Transaction extends ENTITY{

    public static final ISSUANCE = "ISS"
    public static final REDEMPTION = "RDM"
    public static final RETURN = "RTN"


    @Id
    String id

    Integer point

    Integer balance

    String type

    @Column(columnDefinition = "json")
    String additionalFields

    @Version
    private Integer version

    @OneToOne
    Transaction nextTransaction

    @ManyToOne
    Account account
}
