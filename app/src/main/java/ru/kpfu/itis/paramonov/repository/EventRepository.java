package ru.kpfu.itis.paramonov.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.kpfu.itis.paramonov.entity.Event;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    static Specification<Event> buildSpecification(String name, Long place, LocalDateTime fromDate, LocalDateTime toDate) {
        List<Specification<Event>> specifications = new ArrayList<>();
        if (name != null) {
            Specification<Event> specification = (root, query, criteriaBuilder)
                    -> criteriaBuilder.equal(root.get("name"), name);
            specifications.add(specification);
        }
        if (place != null) {
            Specification<Event> specification = (root, query, criteriaBuilder)
                    -> criteriaBuilder.equal(root.get("place_id"), place);
            specifications.add(specification);
        }
        if (toDate != null) {
            Specification<Event> specification = (root, query, criteriaBuilder)
                    -> criteriaBuilder.lessThanOrEqualTo(root.get("to_date"), toDate);
            specifications.add(specification);
        }
        if (fromDate != null) {
            Specification<Event> specification = (root, query, criteriaBuilder)
                    -> criteriaBuilder.lessThanOrEqualTo(root.get("from_date"), fromDate);
            specifications.add(specification);
        }
        return specifications.stream().reduce(Specification::and).orElse(null);
    }
}
