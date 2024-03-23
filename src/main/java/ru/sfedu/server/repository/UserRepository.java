package ru.sfedu.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sfedu.server.model.user.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(long id);
    Optional<User> findByUid(String uid);
}
