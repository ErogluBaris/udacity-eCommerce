package com.example.demo;

import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    public void createUser_whenRequestValid() {
        CreateUserRequest createUserRequest = createUserRequest();
        final ResponseEntity<User> response = userController.createUser(createUserRequest);

        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();
        Assert.assertNotNull(u);
        Assert.assertEquals(0, u.getId());
        Assert.assertEquals("test", u.getUsername());
    }

    @Test
    public void createUser_whenRequestPasswordNotEqualsConfirmPassword() {
        CreateUserRequest createUserRequest = createUserRequest();
        createUserRequest.setConfirmPassword("test123456");

        final ResponseEntity<User> response = userController.createUser(createUserRequest);

        Assert.assertEquals(400, response.getStatusCodeValue());

        User u = response.getBody();

        Assert.assertNull(u);
    }

    @Test
    public void getUserByName_whenUserExists() {

        String testUsername = "test";

        User testUser = new User();
        testUser.setId(0);
        testUser.setUsername(testUsername);

        Mockito.when(userRepository.findByUsername("test")).thenReturn(testUser);

        ResponseEntity<User> response = userController.findByUserName(testUsername);
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();
        Assert.assertNotNull(u);
        Assert.assertEquals(testUser.getId(), u.getId());
        Assert.assertEquals(testUser.getUsername(), u.getUsername());
    }

    @Test
    public void getUserByName_whenUserNotExists() {

        CreateUserRequest createUserRequest = createUserRequest();
        userController.createUser(createUserRequest);

        Mockito.when(userRepository.findByUsername("test")).thenReturn(null);

        ResponseEntity<User> response = userController.findByUserName(createUserRequest.getUsername());
        Assert.assertNotNull(response);
        Assert.assertEquals(404, response.getStatusCodeValue());

        User u = response.getBody();
        Assert.assertNull(u);
    }

    @Test
    public void getUserById_whenUserExists() {

        User testUser = new User();
        testUser.setId(0);
        testUser.setUsername("test");

        Mockito.when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));

        ResponseEntity<User> response = userController.findById(testUser.getId());
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();
        Assert.assertNotNull(u);
        Assert.assertEquals(testUser.getId(), u.getId());
        Assert.assertEquals(testUser.getUsername(), u.getUsername());
    }

    @Test
    public void getUserById_whenUserNotExists() {

        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        ResponseEntity<User> response = userController.findById(0L);
        Assert.assertNotNull(response);
        Assert.assertEquals(404, response.getStatusCodeValue());

        User u = response.getBody();
        Assert.assertNull(u);
    }

    private CreateUserRequest createUserRequest() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("testPassword");
        createUserRequest.setConfirmPassword("testPassword");

        return createUserRequest;
    }
}
