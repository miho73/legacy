package com.github.miho73.legacy.service;

import com.github.miho73.legacy.jpa.User;
import com.github.miho73.legacy.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
@Slf4j
public class AuthService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    public boolean existsUserById(String id) {
        return userRepository.findById(id).isEmpty();
    }

    public User createUser(String name, int code, String id, String pwd) {
        User user = new User();
        user.setName(name);
        user.setCode(code);
        user.setId(id);
        user.setPwd(passwordEncoder.encode(pwd));
        user.setJoinDate(new Timestamp(System.currentTimeMillis()));
        user.setStatus(User.USER_STATUS.UNACTIVATED);
        return userRepository.save(user);
    }

    /**
     * check user activation status
     * @param id
     * @param pwd
     * @return 0 when activated, 1 when not activated, 2 when not exists, 3 when auth fail
     */
    public int checkActiveStatus(String id, String pwd) {
        List<User> users = userRepository.findById(id);
        if(users.size() != 1) return 3;
        User user = users.get(0);
        if(!passwordEncoder.matches(pwd, user.getPwd())) {
            return 3;
        }
        if(user.getStatus() == User.USER_STATUS.ACTIVATED) return 0;
        if(user.getStatus() == User.USER_STATUS.UNACTIVATED) return 1;
        if(user.getStatus() == User.USER_STATUS.BANNED) return 2;
        else return 1;
    }
}
