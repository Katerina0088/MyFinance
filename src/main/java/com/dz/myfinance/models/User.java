package com.dz.myfinance.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Data
@Table(name = "users")
public class User  implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private Boolean isAdmin = false;
    private String password;
    private String role;
    public User() {}
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;}
    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
    public void setPassword(String password) {
        this.password = password;}
    public String getPassword() {
        return password;}
    public void setRole(String role) {
        this.role = role;}
    public String getRole() {
        return role;}
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role);
        return List.of(grantedAuthority);}
    @Override
    public boolean isAccountNonExpired() {
        return true;}
    @Override
    public boolean isAccountNonLocked() {
        return true;}
    @Override
    public boolean isCredentialsNonExpired() {
        return true;}
    @Override
    public boolean isEnabled() {
        return true;}
}