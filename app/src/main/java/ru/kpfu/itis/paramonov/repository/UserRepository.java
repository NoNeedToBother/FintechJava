package ru.kpfu.itis.paramonov.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kpfu.itis.paramonov.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
