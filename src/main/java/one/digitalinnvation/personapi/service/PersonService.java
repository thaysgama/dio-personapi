package one.digitalinnvation.personapi.service;

import one.digitalinnvation.personapi.dto.request.PersonDTO;
import one.digitalinnvation.personapi.dto.response.MessageResponseDTO;
import one.digitalinnvation.personapi.entity.Person;
import one.digitalinnvation.personapi.exception.PersonNotFoundException;
import one.digitalinnvation.personapi.mapper.PersonMapper;
import one.digitalinnvation.personapi.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonService {
    private final PersonRepository personRepository;

    private final PersonMapper personMapper = PersonMapper.INSTANCE;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public MessageResponseDTO createPerson(@RequestBody PersonDTO personDTO){
        Person personToSave = personMapper.toModel(personDTO);
        Person savedPerson = personRepository.save(personToSave);
        return MessageResponseDTO
                .builder()
                .message("Created person with ID "+ savedPerson.getId())
                .build();
    }

    public List<PersonDTO> listAll(){
        return personRepository.findAll().stream()
                .map(personMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PersonDTO findById(Long id) throws PersonNotFoundException {
        Optional<Person> optionalPerson = personRepository.findById(id);
        if(optionalPerson.isEmpty())
            throw new PersonNotFoundException(id);
        return personMapper.toDTO(optionalPerson.get());
    }
}
