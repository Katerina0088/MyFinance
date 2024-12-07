package com.dz.myfinance.repositories;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;


public interface UserRepository extends CrudRepository<com.dz.myfinance.models.User, Long> {
    @Query("SELECT u FROM User u WHERE CONCAT(u.id, u.username, u.role, u.password) LIKE %?1%")
    public Iterable<com.dz.myfinance.models.User> search(String keyword);

    List<com.dz.myfinance.models.User> findAll();

    com.dz.myfinance.models.User findByUsername(String username) throws UsernameNotFoundException;
}