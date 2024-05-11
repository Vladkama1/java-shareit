package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.context.annotation.Import;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@Import(LocalValidatorFactoryBean.class)
class UserDtoTest {
    @Autowired
    private JacksonTester<UserDto> json;

    @Autowired
    private Validator validator;

    @Test
    void testUserDtoSerialization() throws Exception {
        UserDto userDTO = UserDto.builder().id(1L).email("test@mail.ru").name("test").build();

        JsonContent<UserDto> result = json.write(userDTO);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("test");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("test@mail.ru");
    }


    @Test
    void testValidationOnUpdate() {
        UserDto userDTO = UserDto.builder().name("test").email("test@mail.ru").build();

        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDTO);
        assertThat(violations).isEmpty();
    }

    @Test
    void testInvalidEmailOnCreate() {
        UserDto userDTO = UserDto.builder().name("test").email("invalid_email").build();

        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDTO);
        assertThat(violations).isNotEmpty();
    }

    @Test
    public void testGettersAndSetters() {
        UserDto user = UserDto.builder()
                .id(null)
                .name("John Doe")
                .email("john.doe@example.com")
                .build();

        assertThat(user.getId()).isNull();
        assertThat(user.getName()).isEqualTo("John Doe");
        assertThat(user.getEmail()).isEqualTo("john.doe@example.com");
    }
}