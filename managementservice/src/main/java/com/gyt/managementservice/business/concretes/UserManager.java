package com.gyt.managementservice.business.concretes;

import com.gyt.managementservice.business.abstracts.UserService;
import com.gyt.managementservice.business.dtos.requests.RegisterRequest;
import com.gyt.managementservice.dataAccess.abstracts.RoleRepository;
import com.gyt.managementservice.dataAccess.abstracts.UserRepository;
import com.gyt.managementservice.entities.concretes.Role;
import com.gyt.managementservice.entities.concretes.User;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@AllArgsConstructor
@Service
public class UserManager implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;


    //todo: hata gözden geçirilecek
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() -> new AccessDeniedException("Giriş başarısız."));
    }

    @Override
    public void add(RegisterRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        Role role = roleRepository.findById(2).orElseThrow();
        user.setAuthorities(Set.of(role));
        userRepository.save(user);
    }

    public User getUserPrincipals(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof UserDetails userDetails) {
            return userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        }else {
            throw new RuntimeException("Kullanıcı Bulunamadı");
        }
    }






}
