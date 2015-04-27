package com.kjt.service.common.auth;

import java.math.BigInteger;
import java.util.Map;

public interface UserService {
    public AuthResponse selectList(Map<String, Object> params);

    public AuthResponse insert(Map<String, Object> params);

    public AuthResponse updateById(Map<String, Object> params);

    public AuthResponse deleteById(BigInteger id);

    public AuthResponse selectList(Map<String, Object> params, int offset, int pageSize);

    public boolean isUserNameExist(String userName);

    public AuthResponse<AuthUserDto> selectById(BigInteger id);

    public boolean isAllowDelele(BigInteger id);

    public AuthUserDto selectByTicket(String ticketValidateUrl, String ticket);
}
