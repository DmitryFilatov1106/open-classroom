package ru.fildv.openclassroomservice.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import ru.fildv.openclassroomdb.dao.UserDao;
import ru.fildv.openclassroomdb.dto.user.CreateUserDto;
import ru.fildv.openclassroomdb.dto.user.UpdateUserDto;
import ru.fildv.openclassroomdb.dto.user.UserDto;
import ru.fildv.openclassroomdb.mapper.user.CreateUserMapper;
import ru.fildv.openclassroomdb.mapper.user.UpdateUserMapper;
import ru.fildv.openclassroomdb.mapper.user.UserMapper;
import ru.fildv.openclassroomservice.exception.ValidationException;
import ru.fildv.openclassroomservice.validator.CreateUserValidator;

import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserService {
    private static final UserService INSTANCE = new UserService();
    private final CreateUserValidator createUserValidator
            = CreateUserValidator.getInstance();
    private final UserDao userDao = UserDao.getInstance();
    private final CreateUserMapper createUserMapper
            = CreateUserMapper.getInstance();
    private final UpdateUserMapper updateUserMapper
            = UpdateUserMapper.getInstance();
    private final ImageService imageService = ImageService.getInstance();
    private final UserMapper userMapper = UserMapper.getInstance();

    public static UserService getInstance() {
        return INSTANCE;
    }

    public Optional<UserDto> login(final String email, final String password) {
        return userDao.findByEmailAndPassword(email, password)
                .map(userMapper::mapFrom);
    }

    @SneakyThrows
    public Integer create(final CreateUserDto createUserDto) {
        var validationResult = createUserValidator.isValid(createUserDto);

        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }

        var userEntity = createUserMapper.mapFrom(createUserDto);
        if (createUserDto.image().getSize() > 0) {
            imageService.upload(userEntity.getImage(),
                    createUserDto.image().getInputStream());
        }
        userDao.save(userEntity);
        return userEntity.getId();
    }

    @SneakyThrows
    public void update(final UpdateUserDto updateUserDto) {
        var validationResult = createUserValidator.isValid(
                new CreateUserDto(updateUserDto.username(),
                        updateUserDto.birthday(),
                        updateUserDto.image(),
                        updateUserDto.email(),
                        updateUserDto.password(),
                        updateUserDto.role(),
                        updateUserDto.gender()
                ));

        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }

        var userEntity = updateUserMapper.mapFrom(updateUserDto);
        if (updateUserDto.image().getSize() > 0) {
            imageService.upload(userEntity.getImage(),
                    updateUserDto.image().getInputStream());
        }
        userDao.update(userEntity);
    }

    public Optional<UserDto> findById(final Integer id) {
        return userDao.findById(id)
                .map(userMapper::mapFrom);
    }

    public List<UserDto> findAll() {
        return userDao.findAll().stream()
                .map(userMapper::mapFrom)
                .toList();
    }
}
