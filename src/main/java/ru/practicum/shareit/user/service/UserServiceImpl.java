package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.model.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto create(User user) {
        return UserMapper.toUserDto(userRepository.create(user));
    }

    @Override
    public UserDto update(long id, Map<String, String> updates) {
        return UserMapper.toUserDto(userRepository.update(id, updates));
    }

    @Override
    public UserDto getUser(long id) {
        return UserMapper.toUserDto(userRepository.getUser(id));
    }

    @Override
    public List<UserDto> getAll() {
        return UserMapper.toUserDtoList(userRepository.getAll());
    }

    @Override
    public void delete(long userId) {
        userRepository.delete(userId);
    }

    @Override
    public void deleteAll() {
        userRepository.deleteAll();
    }

}
