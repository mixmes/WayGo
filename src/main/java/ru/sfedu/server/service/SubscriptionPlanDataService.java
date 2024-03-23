package ru.sfedu.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sfedu.server.model.subscription.SubscriptionPlan;
import ru.sfedu.server.repository.SubscriptionPlanRepository;

import java.util.Optional;

@Service
public class SubscriptionPlanDataService {
    @Autowired
    private SubscriptionPlanRepository repository;

    public void save(SubscriptionPlan plan) {
        repository.save(plan);
    }

    public void delete(SubscriptionPlan plan) {
        repository.delete(plan);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public Optional<SubscriptionPlan> getById(Long id) {
        return repository.findById(id);
    }
}
