package com.nidyran.rolebasedspringsecurity.dao.repository;

import com.nidyran.rolebasedspringsecurity.dao.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}