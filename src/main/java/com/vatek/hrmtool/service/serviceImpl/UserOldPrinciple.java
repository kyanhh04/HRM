package com.vatek.hrmtool.service.serviceImpl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vatek.hrmtool.entity.Config;
import com.vatek.hrmtool.entity.UserOld;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.Objects;

@Getter
@Setter
@Builder(builderMethodName = "internalBuilder")
public class UserOldPrinciple implements UserDetails {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;

    private String name;

    private String username;

    private String email;
    
    @JsonIgnore
    private String password;

    private boolean isEnabled;

    private Long remainTime;

    private String accessToken;

    private Collection<? extends GrantedAuthority> authorities;

    private Collection<String> positions;

    private Collection<String> privileges;


    public static UserOldPrincipleBuilder userOldPrincipleBuilder(UserOld userOld) {

        return internalBuilder()
                .id(userOld.getId())
                .name(userOld.getFullName())
                .username(userOld.getEmail())
                .email(userOld.getEmail())
                .password(userOld.getPasswordHash())
                .isEnabled(!userOld.getIsDeleted())
                .accessToken(userOld.getRefreshToken());
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
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }


    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserOldPrinciple user = (UserOldPrinciple) o;
        return Objects.equals(id, user.id);
    }
}
