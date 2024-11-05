package com.java.currencyexchange.user.mapper;

import com.java.currencyexchange.dto.response.UserDto;
import com.java.currencyexchange.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface UserMapper {
    UserDto toDto(User source);
}
