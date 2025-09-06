package com.nnk.springboot.services.contracts;

import com.nnk.springboot.model.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    User saveUser(User user);

    List<User> getAllUsers();

    Optional<User> findByUsername(String username);

    Optional<User> findById(Integer id);

    void deleteById(Integer user);
}
