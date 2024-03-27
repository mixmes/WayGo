package ru.sfedu.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sfedu.server.model.City;
import ru.sfedu.server.repository.CityRepository;

import java.util.List;

@Service
public class CityDataService {
    @Autowired
    private CityRepository repository;

    public List<City> getCityByName(String city) {
        return repository.searchByCityLike(city);
    }

    public void saveList(List<City> cities) {
        repository.saveAll(cities);
    }

    public void save(City city) {
        repository.save(city);
    }

    public void deleteByCity(String city) {
        repository.deleteByCity(city);
    }
}
