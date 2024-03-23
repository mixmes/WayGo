package ru.sfedu.server.model.subscription;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "ТранзакцииСписанияДенегЗаПодписку")
public class SubscriptionTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private long id;

    @Column(name = "id_user", insertable = false, updatable = false)
    private long userId;

    @Column(name = "subscription_plan_id", insertable = false, updatable = false)
    private long subscriptionPlanId;

    @Column(name = "transaction_date")
    private Date transactionDate;

    @Column(name = "transaction_status")
    private boolean status;


}
