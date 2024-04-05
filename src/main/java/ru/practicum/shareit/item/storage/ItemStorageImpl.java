package ru.practicum.shareit.item.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ItemStorageImpl implements ItemStorage {
    private Long globalId = 0L;
    private final Map<Long, Item> items = new HashMap<>();

    @Override
    public Optional<Item> findById(Long id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public List<Item> getAllItemsOwners(Long ownerId) {
        return items.values().stream()
                .filter(x -> x.getOwner().getId().equals(ownerId))
                .collect(Collectors.toList());
    }

    @Override
    public Item saveItem(Item item) {
        item.setId(creatId());
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public List<Item> searchBy(String text) {
        return items.values()
                .stream()
                .filter(item -> item.getAvailable() &&
                        (item.getName().toLowerCase().contains(text) ||
                                item.getDescription().toLowerCase().contains(text)))
                .collect(Collectors.toList());
    }

    private Long creatId() {
        return ++globalId;
    }
}
