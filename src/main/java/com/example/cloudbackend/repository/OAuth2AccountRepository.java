package com.example.cloudbackend.repository;

import com.example.cloudbackend.domain.OAuth2Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OAuth2AccountRepository extends JpaRepository<OAuth2Account, Long> {
    Optional<OAuth2Account> findByProviderId(String providerId);
}
