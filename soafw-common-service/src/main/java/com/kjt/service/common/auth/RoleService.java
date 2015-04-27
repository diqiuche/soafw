package com.kjt.service.common.auth;

import java.math.BigInteger;
import java.util.Map;

public interface RoleService {
    
    public AuthResponse<AuthRoleDto> selectList(Map<String, Object> params);

    public AuthResponse insert(Map<String, Object> params);
    
    public AuthResponse updateById(Map<String, Object> params);
    
    public AuthResponse deleteById(BigInteger id) ;
    
    public AuthResponse<AuthRoleDto> selectList(Map<String, Object> params, int offset,
        int pageSize);
    
    public AuthResponse<AuthRoleDto> selectByUserId(BigInteger userId);
    
    public AuthResponse<AuthRoleDto> selectById(BigInteger id);
    
    public AuthResponse addUserRole(BigInteger userId, BigInteger roleId);
    
    public AuthResponse delUserRole(BigInteger userId, BigInteger roleId);
}
