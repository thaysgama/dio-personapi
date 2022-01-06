package one.digitalinnvation.personapi.service;

import one.digitalinnvation.personapi.dto.request.PersonDTO;
import one.digitalinnvation.personapi.dto.response.MessageResponseDTO;
import one.digitalinnvation.personapi.entity.Person;
import one.digitalinnvation.personapi.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static one.digitalinnvation.personapi.utils.PersonUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonService personService;

    @Test
    void testGivenPersonDTOThenReturnSavedMessage() {
        PersonDTO personDTO = createFakeDTO();
        Person expectedSavedPerson = createFakeEntity();
        when(personRepository.save(any(Person.class)))
                .thenReturn(expectedSavedPerson);
        MessageResponseDTO expectedSucessMessage = createExpectedMessage(expectedSavedPerson.getId());
        MessageResponseDTO sucessMessage = personService.createPerson(personDTO);
        assertEquals(expectedSucessMessage,sucessMessage);
    }

    private MessageResponseDTO createExpectedMessage(Long id) {
        return MessageResponseDTO
                .builder()
                .message("Created person with ID "+ id)
                .build();
    }
}