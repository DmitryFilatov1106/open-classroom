package ru.fildv.openclassroomdb.mapper.user;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.fildv.openclassroomdb.dto.user.StudentDto;
import ru.fildv.openclassroomdb.entity.Student;
import ru.fildv.openclassroomdb.mapper.Mapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StudentMapper implements Mapper<Student, StudentDto> {
    private static final StudentMapper INSTANCE = new StudentMapper();

    public static StudentMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public StudentDto mapFrom(final Student object) {
        return new StudentDto(
                object.getId(),
                object.getUsername(),
                object.getEmail(),
                object.getGrade()
        );
    }
}
