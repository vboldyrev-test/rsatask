package com.vboldyrev.interview.services;

import com.vboldyrev.interview.repository.UserRepository;
import com.vboldyrev.interview.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Service
public class UserService {

    private static final ThreadLocal<SimpleDateFormat> dateFormatHolder =
            ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"));

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Cacheable(value="userDate")
    public Optional<String> getUserDate(String name) {
        Optional<Date> optionalDate = userRepository.findById(name).map(User::getDate);
        return optionalDate.map(date -> dateFormatHolder.get().format(date));
    }

    @CacheEvict(value="userDate", key="#name")
    public void clearUserCacheByName(String name){}

    public void updateUserDate(String name) {
        userRepository.save(new User(name, new Date()));
    }
}
