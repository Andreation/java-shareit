package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.model.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceDb implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto create(User user) {
        log.trace("create user..");
        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserDto update(long id, Map<String, String> updates) {
        log.trace("update user..");

        User user = getUser(id);
        if (updates.containsKey("name")) {
            String name = updates.get("name");
            user.setName(name.trim());
            log.trace("name changed..");
        }
        if (updates.containsKey("email")) {
            String email = updates.get("email");
            user.setEmail(email);
            log.trace("email changed..");
        }
        userRepository.save(user);
        log.trace("user saved..");
        return UserMapper.toUserDto(getUser(id));
    }

    @Transactional(readOnly = true)
    @Override
    public User getUser(long id) {
        log.trace("get user..");
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("user not found"));
    }

    @Override
    public UserDto getUserDto(long id) {
        log.trace("get UserDto..");
        return UserMapper.toUserDto(getUser(id));
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> getAll() {
        log.trace("getAll..");
        return UserMapper.toUserDtoList(userRepository.findAll());
    }

    @Transactional
    @Override
    public void delete(long userId) {
        log.trace("delete user..");
        userRepository.deleteById(userId);
    }

}
