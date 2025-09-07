package com.nnk.springboot.unitaire;

import com.nnk.springboot.model.User;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1);
        user.setUsername("testuser");
        user.setPassword("plainPassword");
    }

    @Test
    void saveUser_ShouldEncodePasswordAndSave() {
        String encodedPassword = "encodedPassword";

        when(passwordEncoder.encode(user.getPassword())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User savedUser = userService.saveUser(user);

        verify(passwordEncoder, times(1)).encode("plainPassword");
        verify(userRepository, times(1)).save(user);

        assertEquals(encodedPassword, savedUser.getPassword());
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers() {
        List<User> users = Arrays.asList(user, new User());

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        verify(userRepository, times(1)).findAll();
        assertEquals(2, result.size());
    }

    @Test
    void findByUsername_ShouldReturnUser() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findByUsername("testuser");

        verify(userRepository, times(1)).findByUsername("testuser");
        assertTrue(foundUser.isPresent());
        assertEquals("testuser", foundUser.get().getUsername());
    }

    @Test
    void findById_ShouldReturnUser() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findById(1);

        verify(userRepository, times(1)).findById(1);
        assertTrue(foundUser.isPresent());
        assertEquals(1, foundUser.get().getId());
    }

    @Test
    void deleteById_ShouldCallDelete() {
        doNothing().when(userRepository).deleteById(1);

        userService.deleteById(1);

        verify(userRepository, times(1)).deleteById(1);
    }
}
