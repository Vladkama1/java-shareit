package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentOutDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommentMapperTest {
    private CommentMapper commentMapper;

    @BeforeEach
    void setup() {
        commentMapper = Mappers.getMapper(CommentMapper.class);
    }

    @Test
    void shouldReturnNullWhenInputIsNull() {
        assertNull(commentMapper.toModelCom(null));
        assertNull(commentMapper.toOutDTOCom(null));
        assertNull(commentMapper.toListOutDTOCom(null));
    }

    @Test
    void shouldMapCommentDtoToComment() {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setAuthorId(2L);
        commentDto.setItemId(3L);
        commentDto.setText("Great item!");
        commentDto.setCreated(LocalDateTime.now());

        Comment comment = commentMapper.toModelCom(commentDto);

        assertNotNull(comment);
        assertEquals(commentDto.getId(), comment.getId());
        assertEquals(commentDto.getText(), comment.getText());
        assertEquals(commentDto.getCreated(), comment.getCreated());
        assertNotNull(comment.getAuthor());
        assertEquals(commentDto.getAuthorId(), comment.getAuthor().getId());
        assertNotNull(comment.getItem());
        assertEquals(commentDto.getItemId(), comment.getItem().getId());
    }

    @Test
    void shouldMapCommentToCommentOutDto() {
        User author = new User();
        author.setId(2L);
        author.setName("Jane Doe");

        Item item = new Item();
        item.setId(3L);

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setAuthor(author);
        comment.setItem(item);
        comment.setText("Great item!");
        comment.setCreated(LocalDateTime.now());

        CommentOutDto commentOutDto = commentMapper.toOutDTOCom(comment);

        assertNotNull(commentOutDto);
        assertEquals(comment.getId(), commentOutDto.getId());
        assertEquals(comment.getText(), commentOutDto.getText());
        assertEquals(comment.getCreated(), commentOutDto.getCreated());
        assertEquals(author.getName(), commentOutDto.getAuthorName());
    }

    @Test
    void shouldMapCommentListToCommentOutDtoList() {
        User author = new User();
        author.setId(2L);
        author.setName("Jane Doe");

        Item item = new Item();
        item.setId(3L);

        Comment comment1 = new Comment();
        comment1.setId(1L);
        comment1.setAuthor(author);
        comment1.setItem(item);
        comment1.setText("Great item!");
        comment1.setCreated(LocalDateTime.now());

        Comment comment2 = new Comment();
        comment2.setId(2L);
        comment2.setAuthor(author);
        comment2.setItem(item);
        comment2.setText("Must have!");
        comment2.setCreated(LocalDateTime.now().minusDays(1));

        List<Comment> comments = Arrays.asList(comment1, comment2);

        List<CommentOutDto> commentOutDtos = commentMapper.toListOutDTOCom(comments);

        assertNotNull(commentOutDtos);
        assertEquals(2, commentOutDtos.size());
        assertEquals(comments.get(0).getId(), commentOutDtos.get(0).getId());
        assertEquals(comments.get(1).getId(), commentOutDtos.get(1).getId());
        assertEquals(comments.get(0).getText(), commentOutDtos.get(0).getText());
        assertEquals(comments.get(1).getText(), commentOutDtos.get(1).getText());
        assertEquals(author.getName(), commentOutDtos.get(0).getAuthorName());
        assertEquals(author.getName(), commentOutDtos.get(1).getAuthorName());
    }
}