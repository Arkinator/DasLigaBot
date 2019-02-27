package com.planed.ctlBot.domain;

import com.planed.ctlBot.data.UserEntity;
import com.planed.ctlBot.data.repositories.UserEntityRepository;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.StreamUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserRepository {
    private final Mapper mapper = new DozerBeanMapper();
    private UserEntityRepository userEntityRepository;

    @Autowired
    public UserRepository(UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }

    public List<User> findAll() {
        final List<User> result = new ArrayList<>();
        StreamUtils.createStreamFromIterator(userEntityRepository.findAll().iterator())
                .map(o -> mapFromEntity(o))
                .forEach(result::add);
        return result;
    }

    public User findByDiscordId(final String discordId) {
        return userEntityRepository
                .findById(discordId)
                .map(userEntity -> mapFromEntity(userEntity))
                .orElse(null);
    }

    public void save(final User... users) {
        for (final User user : users) {
            final UserEntity userEntity = mapToEntity(user);
            userEntityRepository.save(userEntity);
        }
    }

    private UserEntity mapToEntity(final User user) {
        final UserEntity result = mapper.map(user, UserEntity.class);
        result.setMatchId(user.getMatchId());
        return result;
    }

    private User mapFromEntity(final UserEntity user) {
        final User result = mapper.map(user, User.class);
        return result;
    }

    public User refresh(final User user) {
        return userEntityRepository.findById(user.getDiscordId())
                .map(userEntity -> mapFromEntity(userEntity))
                .orElseThrow(() -> new RuntimeException("Error while refreshing user: " + user.toString()));
    }
}
