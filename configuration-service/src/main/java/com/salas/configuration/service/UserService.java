package com.salas.configuration.service;

import com.salas.configuration.dto.UserDto;
import com.salas.configuration.entities.User;
import com.salas.configuration.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepo userRepo;

    @Autowired
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public List<UserDto> findAll() {
        return userRepo.findAll()
                .stream()
                .map(UserService::convert)
                .toList();
    }

    public UserDto findById(Long id) {
        return convert(userRepo.findById(id).orElseThrow(() -> new RuntimeException("User with id " + id + " not found!")));
    }

    public void deleteById(Long id) {
        userRepo.deleteById(id);
    }

    public UserDto create(UserDto userDto) {
        return convert(userRepo.save(convert(userDto)));
    }

    private static UserDto convert(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail(), user.getPhone());
    }

    private static User convert(UserDto userDto) {
        return new User(userDto.name(), userDto.email(), userDto.phone());
    }
}
