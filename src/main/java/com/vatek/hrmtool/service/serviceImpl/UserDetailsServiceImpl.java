package com.vatek.hrmtool.service.serviceImpl;


import com.vatek.hrmtool.entity.Config;
import com.vatek.hrmtool.entity.UserOld;
// import com.vatek.hrmtool.respository.RoleRepository;
// import com.vatek.hrmtool.respository.UserRepository;
import com.vatek.hrmtool.respository.old.UserOldRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    // private UserRepository userRepository;
    private UserOldRepository userOldRepository;

    @Override
    @Transactional
    public UserOldPrinciple loadUserByUsername(String username)
            throws UsernameNotFoundException {

        UserOld userOld = userOldRepository.findByEmail(username)
                .or(() -> userOldRepository.findByUsername(username))
                .orElseThrow(() ->
                        new UsernameNotFoundException("User Not Found with -> username or email : " + username)
                );

        List<Config> positionsConfig = userOld.getPositions();
        List<String> positionsAuthority = new ArrayList<>();
        if (positionsConfig != null) {
            for (Config config : positionsConfig) {
                positionsAuthority.add(config.getValue());
            }
        }

        return UserOldPrinciple
                .userOldPrincipleBuilder(userOld)
                .authorities(getAuthorities(positionsAuthority))
                .positions(positionsAuthority)
                .build();
    }

    private Collection<? extends GrantedAuthority> getAuthorities(List<String> privileges) {
        return getGrantedAuthorities(privileges);
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }
}
