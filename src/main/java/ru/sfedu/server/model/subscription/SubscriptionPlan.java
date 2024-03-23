package ru.sfedu.server.model.subscription;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "ПланПодписки")
public class SubscriptionPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscription_plan_id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "cost")
    private double cost;

    @Column(name = "duration")
    private long duration;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subscription_plan_id")
    private Subscription subscription;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "subscription_plan_id")
    private Set<SubscriptionTransaction> transactions = new HashSet<>();
}
