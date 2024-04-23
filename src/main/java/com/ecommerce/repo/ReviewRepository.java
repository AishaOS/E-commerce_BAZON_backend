package com.ecommerce.repo;

import com.ecommerce.entity.Reviews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Reviews, Long> {
    List<Reviews> findAllByProductId(Long productId);
}
