package com.example.demo;

import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemControllerTest {

    @InjectMocks
    private ItemController itemController;

    @Mock
    private ItemRepository itemRepository;

    @Test
    public void getItemsById_whenRequestValid() {
        Mockito.when(itemRepository.findAll()).thenReturn(new ArrayList<>());
        ResponseEntity<List<Item>> response = itemController.getItems();
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(0, response.getBody().size());
    }

    @Test
    public void getItemById_whenRequestValid() {

        Item testItem = new Item();
        testItem.setId(0L);
        testItem.setName("testItem");

        Mockito.when(itemRepository.findById(testItem.getId())).thenReturn(Optional.of(testItem));

        ResponseEntity<Item> response = itemController.getItemById(0L);
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void getItemByName_whenRequestValid() {

        Item testItem = new Item();
        testItem.setId(0L);
        testItem.setName("testItem");

        List<Item> items = new ArrayList<>();
        items.add(testItem);

        Mockito.when(itemRepository.findByName(testItem.getName())).thenReturn(items);

        ResponseEntity<List<Item>> response = itemController.getItemsByName(testItem.getName());
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void getItemByName_whenRequestNotValid() {

        Mockito.when(itemRepository.findByName(ArgumentMatchers.anyString())).thenReturn(new ArrayList<>());

        ResponseEntity<List<Item>> response = itemController.getItemsByName("test");
        Assert.assertNotNull(response);
        Assert.assertEquals(404, response.getStatusCodeValue());
    }
}
