package com.kjt.service.common.web.security;

import com.kjt.service.common.util.MD5Util;

public class MD5Encoder
        implements
            org.springframework.security.crypto.password.PasswordEncoder,
            org.springframework.security.authentication.encoding.PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        return MD5Util.md5Hex(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return MD5Util.md5Hex(rawPassword.toString()).equals(encodedPassword);
    }

    public String encodePassword(String origPwd, Object salt) {
        return MD5Util.md5Hex(origPwd);
    }

    public boolean isPasswordValid(String encPwd, String origPwd, Object salt) {
        return MD5Util.md5Hex(origPwd).equals(encPwd);
    }
}
