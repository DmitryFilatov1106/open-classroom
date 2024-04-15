package ru.fildv.openclassroomservice.validator;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.fildv.openclassroomdb.dto.user.CreateUserDto;
import ru.fildv.openclassroomdb.entity.Gender;
import ru.fildv.openclassroomdb.entity.Role;
import ru.fildv.openclassroomutil.util.LocalDateFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateUserValidator implements Validator<CreateUserDto> {
    private static final CreateUserValidator INSTANCE
            = new CreateUserValidator();

    public static CreateUserValidator getInstance() {
        return INSTANCE;
    }

    @Override
    public ValidationResult isValid(final CreateUserDto createUserDto) {
        var validationResult = new ValidationResult();

        if (createUserDto.username().length() < 4) {
            validationResult.add(new Error("invalid.username",
                    "Size Username must be more than 3 symbols"));
        }

        if (LocalDateFormatter.isNotValid(createUserDto.birthday())) {
            validationResult.add(new Error("invalid.birthday",
                    "Birthday is invalid"));
        }

        if (!createUserDto.email()
                .matches("[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$")) {
            validationResult.add(new Error("invalid.email",
                    "Email is not in format!"));
        }

        if (createUserDto.password().length() == 0) {
            validationResult.add(new Error("invalid.password",
                    "Password is empty!"));
        }

        if (Role.find(createUserDto.role()).isEmpty()) {
            validationResult.add(new Error("invalid.role", "Role is invalid"));
        }

        if (Gender.find(createUserDto.gender()).isEmpty()) {
            validationResult.add(new Error("invalid.gender",
                    "Gender is invalid"));
        }
        return validationResult;
    }
}
