package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class RequestRepositoryTest {

    @Autowired
    private TestEntityManager em;
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void whenFindAllByRequesterNotOrderByCreatedDesc_thenRequestsAreFound() {
        User requester = new User();
        requester.setName("Requester Name");
        requester.setEmail("requester@example.com");
        em.persist(requester);
        em.flush();

        User otherUser = new User();
        otherUser.setName("Other User Name");
        otherUser.setEmail("otheruser@example.com");
        em.persist(otherUser);
        em.flush();

        Request request = new Request();
        request.setDescription("Need a drill");
        request.setRequester(otherUser);
        request.setCreated(LocalDateTime.now());
        em.persist(request);
        em.flush();

        PageRequest pageable = PageRequest.of(0, 10);

        Page<Request> requestsPage = requestRepository.findAllByRequesterNotOrderByCreatedDesc(requester, pageable);

        assertEquals(1, requestsPage.getTotalElements());
    }

    @Test
    void whenFindAllByRequesterOrderByCreatedDesc_thenRequestsAreFound() {
        User requester = new User();
        requester.setName("Requester Name");
        requester.setEmail("requester@example.com");
        em.persist(requester);
        em.flush();

        Request request1 = new Request();
        request1.setDescription("Need a drill");
        request1.setRequester(requester);
        request1.setCreated(LocalDateTime.now().minusDays(1));
        em.persist(request1);

        Request request2 = new Request();
        request2.setDescription("Looking for a camera");
        request2.setRequester(requester);
        request2.setCreated(LocalDateTime.now());
        em.persist(request2);
        em.flush();

        List<Request> requestsList = requestRepository.findAllByRequesterOrderByCreatedDesc(requester);

        assertTrue(requestsList.size() >= 2);
        assertTrue(requestsList.get(0).getCreated().isAfter(requestsList.get(1).getCreated()));
    }
}