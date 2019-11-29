package ru.komarov.testtask.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.komarov.testtask.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
