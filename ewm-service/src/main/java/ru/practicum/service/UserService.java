package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.UserCreateDto;
import ru.practicum.dto.UserDto;
import ru.practicum.exception.ValidationIdException;
import ru.practicum.mapper.UserMapper;
import ru.practicum.model.User;
import ru.practicum.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDto createUser(UserCreateDto userRequest) {
        User user = UserMapper.toUser(userRequest);
        User saveUser = userRepository.save(user);
        return UserMapper.toUserDto(saveUser);
    }

    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        List<User> userList = new ArrayList<>();

        if (ids == null || ids.isEmpty()) {
            userList = userRepository.findAll(PageRequest.of(from, size)).getContent();
        } else {
            userList = userRepository.findAllByIdIn(ids, PageRequest.of(from, size)).getContent();
        }

        return userList.stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    public void deleteUser(Long userId) {
        User user = checkUser(userId);
        userRepository.delete(user);
    }

    public User checkUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ValidationIdException("Пользователь с id=" + userId + ", не найден"));
    }
}

