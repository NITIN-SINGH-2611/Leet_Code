package com.codecrackai.backend.mapper;

import com.codecrackai.backend.dto.response.UserProfileResponse;
import com.codecrackai.backend.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-06T22:33:34+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 26.0.1 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserProfileResponse toProfile(User user) {
        if ( user == null ) {
            return null;
        }

        UserProfileResponse.UserProfileResponseBuilder userProfileResponse = UserProfileResponse.builder();

        userProfileResponse.id( user.getId() );
        userProfileResponse.email( user.getEmail() );
        userProfileResponse.fullName( user.getFullName() );
        userProfileResponse.xp( user.getXp() );
        userProfileResponse.level( user.getLevel() );
        userProfileResponse.currentStreak( user.getCurrentStreak() );
        userProfileResponse.longestStreak( user.getLongestStreak() );
        userProfileResponse.targetQuestionsPerDay( user.getTargetQuestionsPerDay() );

        userProfileResponse.role( user.getRole().name() );

        return userProfileResponse.build();
    }
}
