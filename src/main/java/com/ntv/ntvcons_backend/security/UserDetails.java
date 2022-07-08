package com.ntv.ntvcons_backend.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ntv.ntvcons_backend.entities.User;
import com.ntv.ntvcons_backend.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class UserDetails implements org.springframework.security.core.userdetails.UserDetails {
    @Autowired
    RoleRepository roleRepository;

    private static final long serialVersionUID = 1L;

    private Long userID;
    private Collection<? extends GrantedAuthority> authorities;
    @JsonIgnore
    private String username;
    private String password;
    public UserDetails(Long id,String username,String password,
                           Collection<? extends GrantedAuthority> authorities) {
        this.userID=id;
        this.username=username;
        this.password=password;
        this.authorities = authorities;

    }
    public static UserDetails build(User user,String roleName) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(roleName));
        return new UserDetails(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),//user.getPassword(),//TODO : add xong password thi mo comment + bo user.getEmail
                authorities);
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        UserDetails user = (UserDetails) obj;
        return Objects.equals(userID, user.userID);
    }
}
