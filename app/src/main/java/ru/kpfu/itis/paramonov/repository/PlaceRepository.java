package ru.kpfu.itis.paramonov.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.kpfu.itis.paramonov.entity.Place;

import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    @Query("select p from Place p join fetch p.events where p.id = :id")
    Optional<Place> findByIdWithEvents(@Param("id") Long id);
}
