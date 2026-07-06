package com.codecrackai.backend.mapper;

import com.codecrackai.backend.dto.response.UserProfileResponse;
import com.codecrackai.backend.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role", expression = "java(user.getRole().name())")
    UserProfileResponse toProfile(User user);
}
