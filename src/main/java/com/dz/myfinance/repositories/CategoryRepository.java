package com.dz.myfinance.repositories;

import com.dz.myfinance.models.Category;
import com.dz.myfinance.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByUser(User user);

    List<Category> findAllByNameContaining(String name);

    @Modifying
    @Query("UPDATE Category c SET c.name = :name WHERE c.id = :id")
    int updateCategoryName(@Param("id") Long id, @Param("name") String name);

    List<Category> findByUserId(Long userId);

    List<Transaction> findAllByUserId(Long userId);

    @Query("SELECT COUNT(c) FROM Category c WHERE c.user = :user")
    long countCategoriesByUser(User user);
}