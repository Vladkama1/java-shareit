package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemRepositoryTest {

    @Autowired
    private TestEntityManager em;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void whenExistsByIdAndOwner_Id_thenTrue() {
        User owner = new User();
        owner.setName("Owner Name");
        owner.setEmail("owner@example.com");
        em.persist(owner);
        em.flush();

        Item item = new Item();
        item.setName("Item Name");
        item.setDescription("Description");
        item.setAvailable(true);
        item.setOwner(owner);
        em.persist(item);
        em.flush();

        boolean exists = itemRepository.existsByIdAndOwner_Id(item.getId(), owner.getId());

        assertTrue(exists);
    }

    @Test
    void whenFindAllByOwner_IdOrderById_thenItemsAreFound() {
        User owner = new User();
        owner.setName("Owner Name");
        owner.setEmail("owner@example.com");
        em.persist(owner);
        em.flush();

        Item item = new Item();
        item.setName("Item Name");
        item.setDescription("Description");
        item.setAvailable(true);
        item.setOwner(owner);
        em.persist(item);
        em.flush();

        PageRequest pageable = PageRequest.of(0, 10);

        Page<Item> itemsPage = itemRepository.findAllByOwner_IdOrderById(owner.getId(), pageable);

        assertEquals(1, itemsPage.getTotalElements());
    }

    @Test
    void whenFindByDescriptionContainingIgnoreCaseAndAvailableIsTrue_thenItemsAreFound() {
        User owner = new User();
        owner.setName("Owner Name");
        owner.setEmail("owner@example.com");
        em.persist(owner);
        em.flush();

        Item item = new Item();
        item.setName("Item Name");
        item.setDescription("Test Description");
        item.setAvailable(true);
        item.setOwner(owner);
        em.persist(item);
        em.flush();

        PageRequest pageable = PageRequest.of(0, 10);

        Page<Item> itemsPage = itemRepository.findByDescriptionContainingIgnoreCaseAndAvailableIsTrue("test", pageable);

        assertEquals(1, itemsPage.getTotalElements());
    }

    @Test
    void whenFindAllByRequestIn_thenItemsAreFound() {
        User owner = new User();
        owner.setName("Owner Name");
        owner.setEmail("owner@example.com");
        em.persist(owner);
        em.flush();

        Request request = new Request();
        request.setDescription("Request Description");
        em.persist(request);
        em.flush();

        Item item = new Item();
        item.setName("Item Name");
        item.setDescription("Item Description");
        item.setAvailable(true);
        item.setOwner(owner);
        item.setRequest(request);
        em.persist(item);
        em.flush();

        List<Item> items = itemRepository.findAllByRequestIn(Collections.singletonList(request));

        assertEquals(1, items.size());
    }
}