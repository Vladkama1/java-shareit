package ru.practicum.shareit.user.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserOutDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toModel(UserDto userDTO);

    UserDto toDTO(User user);

    UserOutDto toOutDTO(User user);

    List<User> toListModels(List<UserDto> userDTOList);

    List<UserDto> toListDTO(List<User> userList);
}
