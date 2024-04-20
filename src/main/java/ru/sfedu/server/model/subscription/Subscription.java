package ru.sfedu.server.model.subscription;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
//@Entity
//@Table(name = "ОформленнаяПодписка")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscription_id")
    private long id;

    @Column(name = "subscription_plan_id", updatable = false, insertable = false)
    private long subscriptionPlanId;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "subscription_plan_id")
    private SubscriptionPlan plan;

}
