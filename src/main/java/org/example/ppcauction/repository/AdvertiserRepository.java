package org.example.ppcauction.repository;

import org.example.ppcauction.entity.Advertiser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdvertiserRepository extends JpaRepository<Advertiser, Long> {
    List<Advertiser> findByNameContainingIgnoreCaseOrCompanyContainingIgnoreCaseOrderByName(String name, String company);
    boolean existsByEmailIgnoreCase(String email);
}
