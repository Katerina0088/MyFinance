package com.dz.myfinance.repositories;


import com.dz.myfinance.models.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.management.relation.Role;
import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<UserRole, Long> { // Updated to UserRole
    @Query("SELECT u FROM UserRole u WHERE CONCAT(u.id, u.roleName) LIKE %?1%")
    public Iterable<UserRole> search(String keyword);

    @Query("SELECT r FROM UserRole r")
    public List<UserRole> findAllRoles();
}