package com.emr.auth.service;

import com.emr.auth.domain.dto.User;
import com.emr.auth.exception.AppException;
import com.emr.auth.exception.ErrorCode;
import com.emr.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import utils.JtwTokenUtil;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    @Value("${jwt.token.secret}")
    private String key;
    private long expireTimeMs = 1000 * 60 * 60l;


    public String join(String userName, String password) {
        //User 중복 체크
        userRepository.findByUserName(userName)
                .ifPresent(user -> {
                    throw new AppException(ErrorCode.USERNAME_DUPLICATED, userName + "는 이미 있습니다.");
                });
        // 저장
        User user = User.builder()
                .userName(userName)
                .password(encoder.encode(password))
                .build();

        userRepository.save(user);

        return "SUCCESS";
    }
    public String login (String userName, String password){
        //Username 없음
        User selectUser = userRepository.findByUserName(userName)
                .orElseThrow(()->new AppException(ErrorCode.USERNAME_NOT_FOUND, "Username 이 없습니다"));

        //잘못된 password
        if(!encoder.matches(password, selectUser.getPassword())) {
            throw new AppException(ErrorCode.INVALID_PASSWORD, "패스워드가 잘못되었음");
        }

        String token = JtwTokenUtil.createToken(selectUser.getUserName(), key, expireTimeMs);

        return token;
    }

}
