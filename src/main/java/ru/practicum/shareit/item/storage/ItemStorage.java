package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemStorage {
    Optional<Item> findById(Long id);

    List<Item> getAllItemsOwners(Long ownerId);

    Item saveItem(Item item);

    Item updateItem(Item item);


    List<Item> searchBy(String text);
}
