package com.ntv.ntvcons_backend.security;

import com.ntv.ntvcons_backend.entities.Role;
import com.ntv.ntvcons_backend.entities.User;
import com.ntv.ntvcons_backend.repositories.RoleRepository;
import com.ntv.ntvcons_backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameAndIsDeletedIsFalse(username).orElse(null);
        if (user == null){
            throw new UsernameNotFoundException(username);
        }else{
            Role role = roleRepository.getById(user.getRoleId());
            return com.ntv.ntvcons_backend.security.UserDetails.build(user, role.getRoleName());
        }
    }
}
