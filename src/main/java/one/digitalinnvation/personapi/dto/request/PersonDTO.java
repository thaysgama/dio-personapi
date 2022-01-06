package one.digitalinnvation.personapi.dto.request;

import lombok.*;
import one.digitalinnvation.personapi.entity.Phone;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonDTO {

    private Long id;

    @NotEmpty
    @Size(min= 2, max=100)
    private String firstName;

    @NotEmpty
    @Size(min= 2, max=100)
    private String lastName;

    @NotEmpty
    @CPF
    private String cpf;

    private String birthDate;

    @Valid
    @NotEmpty
    private List<Phone> phones;

}
