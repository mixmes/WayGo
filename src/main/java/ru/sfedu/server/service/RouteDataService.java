package ru.sfedu.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sfedu.server.model.route.Route;
import ru.sfedu.server.repository.RouteRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RouteDataService {
    @Autowired
    private RouteRepository repository;

    public void save(Route route) {
        repository.save(route);
    }

    public void delete(Route route) {
        repository.delete(route);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public Optional<Route> getById(Long id) {
        return repository.findById(id);
    }

    public List<Route> getByCity(String city){
        return repository.findByCity(city);
    }
}
