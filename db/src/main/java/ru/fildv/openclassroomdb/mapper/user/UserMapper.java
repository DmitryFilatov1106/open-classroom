package ru.fildv.openclassroomdb.mapper.user;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.fildv.openclassroomdb.dto.user.UserDto;
import ru.fildv.openclassroomdb.entity.User;
import ru.fildv.openclassroomdb.mapper.Mapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper implements Mapper<User, UserDto> {
    private static final UserMapper INSTANCE = new UserMapper();

    public static UserMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public UserDto mapFrom(final User object) {
        return new UserDto(
                object.getId(),
                object.getUsername(),
                object.getBirthday(),
                object.getEmail(),
                object.getImage(),
                object.getRole(),
                object.getGender()
        );
    }
}
