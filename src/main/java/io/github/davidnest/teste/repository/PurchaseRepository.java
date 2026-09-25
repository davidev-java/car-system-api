package io.github.davidnest.teste.repository;

import io.github.davidnest.teste.model.entity.Purchase;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.UUID;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, UUID> {
    Page<Purchase> findByClientId(UUID clientId, Pageable pageable);
}
