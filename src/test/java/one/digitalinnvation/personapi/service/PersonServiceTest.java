package one.digitalinnvation.personapi.service;

import one.digitalinnvation.personapi.dto.request.PersonDTO;
import one.digitalinnvation.personapi.dto.response.MessageResponseDTO;
import one.digitalinnvation.personapi.entity.Person;
import one.digitalinnvation.personapi.exception.PersonNotFoundException;
import one.digitalinnvation.personapi.dto.mapper.PersonMapper;
import one.digitalinnvation.personapi.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static one.digitalinnvation.personapi.utils.PersonUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private PersonMapper personMapper;


    @InjectMocks
    private PersonService personService;

    @Test
    void testGivenPersonDTOThenReturnSuccessSavedMessage() {
        PersonDTO personDTO = createFakeDTO();
        Person expectedSavedPerson = createFakeEntity();
        when(personRepository.save(any(Person.class)))
                .thenReturn(expectedSavedPerson);
        MessageResponseDTO expectedSucessMessage = createExpectedMessage(expectedSavedPerson.getId());
        MessageResponseDTO sucessMessage = personService.createPerson(personDTO);
        assertEquals(expectedSucessMessage,sucessMessage);
    }

    @Test
    void testGivenValidPersonIdThenReturnThisPerson() throws PersonNotFoundException {
        PersonDTO expectedPersonDTO = createFakeDTO();
        Person expectedSavedPerson = createFakeEntity();
        expectedPersonDTO.setId(expectedSavedPerson.getId());
        expectedPersonDTO.getPhones().get(0).setId(expectedSavedPerson.getPhones().get(0).getId());

        when(personRepository.findById(expectedSavedPerson.getId())).thenReturn(Optional.of(expectedSavedPerson));
        //when(personMapper.toDTO(expectedSavedPerson)).thenReturn(expectedPersonDTO);

        PersonDTO personDTO = personService.findById(expectedSavedPerson.getId());

        //assertEquals(expectedPersonDTO, personDTO);

        assertEquals(expectedSavedPerson.getId(), personDTO.getId());
        assertEquals(expectedSavedPerson.getFirstName(), personDTO.getFirstName());
    }

    @Test
    void testGivenInvalidPersonIdThenThrowException() {
        var invalidPersonId = 1L;
        when(personRepository.findById(invalidPersonId))
                .thenReturn(Optional.ofNullable(any(Person.class)));

        assertThrows(PersonNotFoundException.class, () -> personService.findById(invalidPersonId));
    }

    @Test
    void testGivenNoDataThenReturnAllPeopleRegistered() {
        Person expectedSavedPerson = createFakeEntity();
        List<Person> expectedRegisteredPeople = Collections.singletonList(expectedSavedPerson);
        PersonDTO personDTO = createFakeDTO();
        personDTO.setId(expectedSavedPerson.getId());

        when(personRepository.findAll()).thenReturn(expectedRegisteredPeople);
        //when(personMapper.toDTO(any(Person.class))).thenReturn(personDTO);

        List<PersonDTO> expectedPeopleDTOList = personService.listAll();

        assertFalse(expectedPeopleDTOList.isEmpty());
        assertEquals(expectedPeopleDTOList.get(0).getId(), personDTO.getId());
    }

    @Test
    void testGivenValidPersonIdAndUpdateInfoThenReturnSuccesOnUpdate() throws PersonNotFoundException {
        var updatedPersonId = 2L;

        PersonDTO updatePersonDTORequest = createFakeDTO();
        updatePersonDTORequest.setId(updatedPersonId);
        updatePersonDTORequest.setLastName("Peleias updated");

        Person expectedPersonToUpdate = createFakeEntity();
        expectedPersonToUpdate.setId(updatedPersonId);

        Person expectedPersonUpdated = createFakeEntity();
        expectedPersonUpdated.setId(updatedPersonId);
        expectedPersonToUpdate.setLastName(updatePersonDTORequest.getLastName());

        when(personRepository.findById(updatedPersonId)).thenReturn(Optional.of(expectedPersonUpdated));
        //when(personMapper.toModel(updatePersonDTORequest)).thenReturn(expectedPersonUpdated);
        when(personRepository.save(any(Person.class))).thenReturn(expectedPersonUpdated);

        MessageResponseDTO successMessage = personService.updateById(updatedPersonId, updatePersonDTORequest);

        assertEquals("Updated person with ID 2", successMessage.getMessage());
    }

    @Test
    void testGivenInvalidPersonIdAndUpdateInfoThenThrowExceptionOnUpdate() throws PersonNotFoundException {
        var invalidPersonId = 1L;

        PersonDTO updatePersonDTORequest = createFakeDTO();
        updatePersonDTORequest.setId(invalidPersonId);
        updatePersonDTORequest.setLastName("Peleias updated");

        when(personRepository.findById(invalidPersonId))
                .thenReturn(Optional.ofNullable(any(Person.class)));

        assertThrows(PersonNotFoundException.class, () -> personService.updateById(invalidPersonId, updatePersonDTORequest));
    }

    @Test
    void testGivenValidPersonIdThenReturnSuccesOnDelete() throws PersonNotFoundException {
        var deletedPersonId = 1L;
        Person expectedPersonToDelete = createFakeEntity();

        when(personRepository.findById(deletedPersonId)).thenReturn(Optional.of(expectedPersonToDelete));
        personService.deleteById(deletedPersonId);

        verify(personRepository, times(1)).deleteById(deletedPersonId);
    }

    @Test
    void testGivenInvalidPersonIdThenReturnSuccesOnDelete() throws PersonNotFoundException {
        var invalidPersonId = 1L;

        when(personRepository.findById(invalidPersonId))
                .thenReturn(Optional.ofNullable(any(Person.class)));

        assertThrows(PersonNotFoundException.class, () -> personService.deleteById(invalidPersonId));
    }

    private MessageResponseDTO createExpectedMessage(Long id) {
        return MessageResponseDTO
                .builder()
                .message("Created person with ID "+ id)
                .build();
    }
}