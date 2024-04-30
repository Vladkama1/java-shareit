package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    boolean existsByIdAndOwner_Id(Long itemId, Long ownerId);

    List<Item> findAllByOwner_IdOrderById(Long ownerId);

    List<Item> findByDescriptionContainingIgnoreCaseAndAvailableIsTrue(String text);
}
