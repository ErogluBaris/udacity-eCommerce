package com.example.demo;

import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CartControllerTest {

    @InjectMocks
    private CartController cartController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ItemRepository itemRepository;

    @Test
    public void addToCart_whenRequestValid() {

        ModifyCartRequest modifyCartRequest = createModifyCartRequest();

        Cart userCart = new Cart();
        userCart.setId(0L);

        User testUser = new User();
        testUser.setUsername("test");
        testUser.setCart(userCart);

        Item testItem = new Item();
        testItem.setId(0L);
        testItem.setName("testItem");

        Mockito.when(userRepository.findByUsername("test")).thenReturn(testUser);
        Mockito.when(itemRepository.findById(testItem.getId())).thenReturn(Optional.of(testItem));

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void addToCart_whenUserNotFound() {

        ModifyCartRequest modifyCartRequest = createModifyCartRequest();

        Mockito.when(userRepository.findByUsername("test")).thenReturn(null);

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        Assert.assertNotNull(response);
        Assert.assertEquals(404, response.getStatusCodeValue());
        Assert.assertNull(response.getBody());
    }

    @Test
    public void addToCart_whenItemNotFound() {

        ModifyCartRequest modifyCartRequest = createModifyCartRequest();

        Cart userCart = new Cart();
        userCart.setId(0L);

        User testUser = new User();
        testUser.setUsername("test");
        testUser.setCart(userCart);

        Mockito.when(userRepository.findByUsername("test")).thenReturn(testUser);
        Mockito.when(itemRepository.findById(0L)).thenReturn(Optional.empty());

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        Assert.assertNotNull(response);
        Assert.assertEquals(404, response.getStatusCodeValue());
        Assert.assertNull(response.getBody());
    }

    @Test
    public void removeFromCard_whenRequestValid() {

        ModifyCartRequest modifyCartRequest = createModifyCartRequest();
        modifyCartRequest.setQuantity(1);

        Item testItem = new Item();
        testItem.setId(0L);
        testItem.setName("testItem");
        testItem.setPrice(BigDecimal.ZERO);

        Cart userCart = new Cart();
        userCart.setId(0L);
        List<Item> items = new ArrayList<>();
        items.add(testItem);
        userCart.setItems(items);
        userCart.setTotal(BigDecimal.ZERO);

        User testUser = new User();
        testUser.setUsername("test");
        testUser.setCart(userCart);

        Mockito.when(userRepository.findByUsername("test")).thenReturn(testUser);
        Mockito.when(itemRepository.findById(testItem.getId())).thenReturn(Optional.of(testItem));

        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());

        Assert.assertNotNull(response.getBody());
        Assert.assertNotNull(response.getBody().getItems());
        Assert.assertEquals(0, response.getBody().getItems().size());
    }

    private ModifyCartRequest createModifyCartRequest() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setQuantity(0);
        modifyCartRequest.setItemId(0);

        return modifyCartRequest;
    }
}
