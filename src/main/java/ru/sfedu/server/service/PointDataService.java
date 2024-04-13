package ru.sfedu.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sfedu.server.model.metainfo.ArMetaInfo;
import ru.sfedu.server.model.metainfo.AudioMetaInfo;
import ru.sfedu.server.model.point.Point;
import ru.sfedu.server.repository.PointRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PointDataService {
    @Autowired
    private PointRepository repository;

    public void save(Point point) {
        repository.save(point);
    }

    public void delete(Point point) {
        repository.delete(point);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public Optional<Point> getById(Long id) {
        return repository.findById(id);
    }

    public List<Point> getAllByCity(String city) {
        return repository.findByCity(city);
    }

    public List<Point> getByCityAndPointName(String city, String pointName) {
        return repository.findByCityAndPointNameLike(pointName,city);
    }

    public List<Point> getByRouteId(Long id){
        return repository.findByRoutes_id(id);
    }

    public Optional<ArMetaInfo> getArMetaInfoByPointId(Long id){
        return repository.findArMetaInfoByPointId(id);
    }

    public Optional<AudioMetaInfo> getAudioMetaInfoByPointId(Long id){
        return repository.findAudioMetaInfoByPointId(id);
    }

}
