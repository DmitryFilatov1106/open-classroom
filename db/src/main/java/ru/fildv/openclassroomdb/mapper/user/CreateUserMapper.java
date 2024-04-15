package ru.fildv.openclassroomdb.mapper.user;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.fildv.openclassroomdb.dto.user.CreateUserDto;
import ru.fildv.openclassroomdb.entity.Gender;
import ru.fildv.openclassroomdb.entity.Role;
import ru.fildv.openclassroomdb.entity.User;
import ru.fildv.openclassroomdb.mapper.Mapper;
import ru.fildv.openclassroomutil.util.LocalDateFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateUserMapper implements Mapper<CreateUserDto, User> {
    private static final CreateUserMapper INSTANCE = new CreateUserMapper();
    private static final String IMAGE_FOLDER = "users/";

    public static CreateUserMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public User mapFrom(final CreateUserDto object) {
        return User.builder()
                .username(object.username())
                .birthday(LocalDateFormatter.format(object.birthday()))
                .image(IMAGE_FOLDER + object.image().getSubmittedFileName())
                .email(object.email())
                .password(object.password())
                .role(Role.valueOf(object.role()))
                .gender(Gender.valueOf(object.gender()))
                .build();
    }
}
