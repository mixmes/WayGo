package ru.sfedu.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sfedu.server.model.point.Point;
import ru.sfedu.server.model.route.Route;
import ru.sfedu.server.model.user.User;
import ru.sfedu.server.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserDataService {
    @Autowired
    private UserRepository repository;

    public void save(User user) {
        repository.save(user);
    }

    public void delete(User user) {
        repository.delete(user);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public Optional<User> getById(long id) {
        return repository.findById(id);
    }

    public Optional<User> getByUid(String uid){
        return repository.findByUid(uid);
    }

    public List<User> getAll(){
        return repository.findAll();
    }

    public List<Long> getFavouritePointsIds(Long id){
        return repository.findAllFavouritePointsById(id).stream().map(Point::getId).toList();
    }

    public List<Long> getFavouriteRoutesIds(Long id){
        return repository.findAllFavouriteRoutesById(id).stream().map(Route::getId).toList();
    }
}
