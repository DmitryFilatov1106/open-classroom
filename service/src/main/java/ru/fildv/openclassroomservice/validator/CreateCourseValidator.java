package ru.fildv.openclassroomservice.validator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.fildv.openclassroomdb.dto.course.CreateCourseDto;
import ru.fildv.openclassroomdb.entity.Status;
import ru.fildv.openclassroomutil.util.LocalDateFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateCourseValidator implements Validator<CreateCourseDto> {
    private static final CreateCourseValidator INSTANCE
            = new CreateCourseValidator();

    public static CreateCourseValidator getInstance() {
        return INSTANCE;
    }

    @Override
    public ValidationResult isValid(final CreateCourseDto createCourseDto) {
        var validationResult = new ValidationResult();

        if (createCourseDto.name().length() < 4) {
            validationResult.add(new Error("invalid.name",
                    "Size name must be more than 3 symbols"));
        }

        if (createCourseDto.capacity() < 1) {
            validationResult.add(new Error("invalid.capacity",
                    "Capacity is invalid"));
        }

        if (Status.find(createCourseDto.status()).isEmpty()) {
            validationResult.add(new Error("invalid.status",
                    "Status is invalid"));
        }

        boolean fromDateValid = false;
        boolean toDateValid = false;
        if (LocalDateFormatter.isNotValid(createCourseDto.fromDate())) {
            validationResult.add(new Error("invalid.fromdate",
                    "From date is invalid"));
        } else {
            fromDateValid = true;
        }

        if (LocalDateFormatter.isNotValid(createCourseDto.toDate())) {
            validationResult.add(new Error("invalid.todate",
                    "To date is invalid"));
        } else {
            toDateValid = true;
        }

        if (fromDateValid && toDateValid) {
            if (LocalDateFormatter.format(createCourseDto.fromDate())
                    .isAfter(LocalDateFormatter
                            .format(createCourseDto.toDate()))) {
                validationResult.add(
                        new Error("invalid.todate",
                                "From date must be less than To date"));
            }
        }

        return validationResult;
    }
}
