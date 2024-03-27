package ru.sfedu.server.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sfedu.server.model.City;
import ru.sfedu.server.service.CityDataService;

import java.util.List;

@RestController
@RequestMapping("/api/city")
@Slf4j
public class CityRestController {
    @Autowired
    private CityDataService dataService;

    @PostMapping("/all")
    public ResponseEntity<?> saveCities(@RequestBody List<City> cities) {
        log.info("Save cities list");
        dataService.saveList(cities);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody City city) {
        log.info("Save city with name = {}", city.getCity());
        dataService.save(city);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteByCityName(@RequestParam String city) {
        log.info("Delete city with name = {}",city);
        dataService.deleteByCity(city);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<City>> getCityByName(@RequestParam String city){
        log.info("Get city with name like={}",city);
        List<City> cities = dataService.getCityByName(city);
        if(cities.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(cities,HttpStatus.OK);
    }
}
