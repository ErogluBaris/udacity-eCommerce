package com.example.demo;

import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @Test
    public void submit_whenRequestValid() {

        Cart userCart = new Cart();
        userCart.setId(0L);
        userCart.setItems(new ArrayList<>());

        User testUser = new User();
        testUser.setUsername("test");
        testUser.setCart(userCart);

        Mockito.when(userRepository.findByUsername("test")).thenReturn(testUser);

        ResponseEntity<UserOrder> response = orderController.submit(testUser.getUsername());
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void submit_whenRequestNotValid() {

        Mockito.when(userRepository.findByUsername("test")).thenReturn(null);

        ResponseEntity<UserOrder> response = orderController.submit("test");
        Assert.assertNotNull(response);
        Assert.assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void getOrdersForUser_whenRequestValid() {

        User testUser = new User();
        testUser.setUsername("test");

        Mockito.when(userRepository.findByUsername("test")).thenReturn(testUser);
        Mockito.when(orderRepository.findByUser(testUser)).thenReturn(new ArrayList<>());

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(testUser.getUsername());
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void getOrdersForUser_whenRequestNotValid() {

        User testUser = new User();
        testUser.setUsername("test");

        Mockito.when(userRepository.findByUsername("test")).thenReturn(null);

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(testUser.getUsername());
        Assert.assertNotNull(response);
        Assert.assertEquals(404, response.getStatusCodeValue());
    }
}
