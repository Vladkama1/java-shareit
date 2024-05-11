package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingOutDto;
import ru.practicum.shareit.booking.dto.BookingShortOutDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentOutDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOutDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserOutDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.enums.Status.APPROVED;
import static ru.practicum.shareit.booking.enums.Status.REJECTED;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemMapper itemMapper;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;

    @Override
    public ItemOutDto findById(Long userId, Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Товар " + itemId + " не найден"));
        ItemOutDto itemOutDto = itemMapper.toOutDTO(item);
        List<BookingOutDto> bookings = bookingMapper.toListOutDTO(bookingRepository.findAllByItem_IdAndStatusIsNot(itemId, REJECTED));
        List<CommentOutDto> comments = commentMapper.toListOutDTOCom(commentRepository.findAllByItem_IdOrderByCreatedDesc(itemId));
        if (item.getOwner().getId().equals(userId)) {
            LocalDateTime now = LocalDateTime.now();
            itemOutDto.setComments(comments);
            return addLastAndNextBooking(itemOutDto, bookings, now);
        } else {
            itemOutDto.setComments(comments);
            return itemOutDto;
        }
    }

    @Override
    public List<ItemOutDto> getAllItemsOwner(Long ownerId, Integer from, Integer size) {
        isExistUserInDb(ownerId);
        Pageable pageable = PageRequest.of(from / size, size);
        List<ItemOutDto> itemOutDtoList = itemMapper.toListOutDTO(itemRepository.findAllByOwner_IdOrderById(ownerId, pageable).getContent());
        List<Long> itemIds = itemOutDtoList.stream()
                .map(ItemOutDto::getId)
                .collect(Collectors.toList());
        List<BookingOutDto> bookings = bookingMapper.toListOutDTO(bookingRepository.findAllByItem_IdInAndStatusIsNot(itemIds, REJECTED, pageable).getContent());
        Map<Long, List<BookingOutDto>> bookingsWithItemsId = bookings.stream()
                .collect(Collectors.groupingBy(booking -> booking.getItem().getId()));

        LocalDateTime now = LocalDateTime.now();
        for (ItemOutDto itemOutDto : itemOutDtoList) {
            addLastAndNextBooking(itemOutDto, bookingsWithItemsId.get(itemOutDto.getId()), now);
        }
        return itemOutDtoList;
    }

    @Override
    @Transactional
    public ItemOutDto saveItem(ItemDto itemDto, Long userId) {
        isExistUserInDb(userId);
        itemDto.setOwnerId(userId);
        return itemMapper.toOutDTO(itemRepository.save(itemMapper.toModel(itemDto)));
    }

    @Override
    @Transactional
    public ItemOutDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
        isExistUserInDb(userId);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Товар " + itemId + " не найден"));
        if (!item.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Данный пользователь не является владельцем товара!");
        }
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        itemRepository.save(item);
        return itemMapper.toOutDTO(item);
    }

    @Override
    public List<ItemDto> searchBy(String text, Integer from, Integer size) {
        if (text.isEmpty()) {
            return Collections.emptyList();
        }
        Pageable pageable = PageRequest.of(from / size, size);
        return itemRepository.findByDescriptionContainingIgnoreCaseAndAvailableIsTrue(text.toLowerCase(), pageable)
                .stream()
                .map(itemMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CommentOutDto addComment(Long userId, Long itemId, CommentDto commentDto) {
        UserOutDto userOutDto = userMapper.toOutDTO(userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь " + userId + " не найден")));
        ItemOutDto itemOutDto = itemMapper.toOutDTO(itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Товар " + itemId + " не найден")));


        LocalDateTime now = LocalDateTime.now();
        boolean isBookingConfirmed = bookingRepository.existsByItem_IdAndBooker_IdAndStatusAndEndIsBefore(itemId, userId, APPROVED, now);
        if (!isBookingConfirmed) {
            throw new BadRequestException("Владелец и пользователь не покупавший эту вещь, не может оставлять отзыв!");
        }
        commentDto.setCreated(now);
        commentDto.setAuthorId(userId);
        commentDto.setItemId(itemId);
        CommentOutDto commentOutDto = commentMapper.toOutDTOCom(commentRepository.save(commentMapper.toModelCom(commentDto)));
        commentOutDto.setAuthorName(userOutDto.getName());
        return commentOutDto;
    }

    private void isExistUserInDb(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь " + userId + " не найден");
        }
    }

    private ItemOutDto addLastAndNextBooking(ItemOutDto itemOutDto, List<BookingOutDto> bookings, LocalDateTime now) {
        if (bookings == null) {
            return itemOutDto;
        }
        BookingShortOutDto lastBooking = bookingMapper.toShortOutDTO(bookings.stream()
                .filter(booking -> booking.getStart().isBefore(now))
                .max(Comparator.comparing(BookingOutDto::getStart))
                .orElse(null));
        BookingShortOutDto nextBooking = bookingMapper.toShortOutDTO(bookings.stream()
                .filter(booking -> booking.getStart().isAfter(now))
                .min(Comparator.comparing(BookingOutDto::getStart))
                .orElse(null));
        itemOutDto.setLastBooking(lastBooking);
        itemOutDto.setNextBooking(nextBooking);
        return itemOutDto;
    }
}
