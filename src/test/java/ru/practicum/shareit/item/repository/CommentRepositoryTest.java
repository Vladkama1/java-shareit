package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CommentRepositoryTest {

    @Autowired
    private TestEntityManager em;
    @Autowired
    private CommentRepository commentRepository;

    private User user;
    private Item item;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setName("User Name");
        user.setEmail("user@example.com");
        em.persist(user);

        item = new Item();
        item.setName("Item Name");
        item.setDescription("Item Description");
        item.setAvailable(true);
        item.setOwner(user);
        em.persist(item);
    }

    @Test
    void whenFindAllByItem_IdOrderByCreatedDesc_thenCommentsAreFound() {
        Comment comment1 = new Comment();
        comment1.setText("First comment");
        comment1.setItem(item);
        comment1.setAuthor(user);
        comment1.setCreated(LocalDateTime.now().minusDays(1));
        em.persist(comment1);

        Comment comment2 = new Comment();
        comment2.setText("Second comment");
        comment2.setItem(item);
        comment2.setAuthor(user);
        comment2.setCreated(LocalDateTime.now());
        em.persist(comment2);

        em.flush();

        List<Comment> comments = commentRepository.findAllByItem_IdOrderByCreatedDesc(item.getId());

        assertFalse(comments.isEmpty());
        assertEquals(2, comments.size());
        assertEquals("Second comment", comments.get(0).getText());
        assertEquals("First comment", comments.get(1).getText());
    }
}