package ru.practicum.shareit.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.util.List;


public interface RequestRepository extends JpaRepository<Request, Long> {
    Page<Request> findAllByRequesterNotOrderByCreatedDesc(User requester, Pageable pageable);

    List<Request> findAllByRequesterOrderByCreatedDesc(User requester);
}
