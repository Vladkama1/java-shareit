package ru.practicum.shareit.user.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {
    @Test
    void testGettersAndSetters() {
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");

        assertEquals(1, user.getId());
        assertEquals("John Doe", user.getName());
        assertEquals("john.doe@example.com", user.getEmail());
    }

    @Test
    void testToString() {
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");

        String expected = "User(id=1, name=John Doe, email=john.doe@example.com)";
        assertEquals(expected, user.toString());
    }
}