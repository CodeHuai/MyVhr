package com.myhr.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

/**
 * 操作员（对应 vhr 的 Hr 表 hr）
 *
 */
@Getter
@Setter
public class Hr implements UserDetails {

    private Long id;
    private String name;
    private String phone;
    private String telephone;
    private String address;
    private Boolean enabled;
    private String username;
    private String password;
    private String userface;
    private String remark;
    private List<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>(this.roles.size());
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }

    // 账号没有过期
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 账号没被锁
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 密码没过期
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 账号可用
    @Override
    public boolean isEnabled() {
        return this.getEnabled();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hr hr = (Hr) o;
        return Objects.equals(this.getUsername(), hr.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
