package one.digitalinnvation.personapi.dto;

import one.digitalinnvation.personapi.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
