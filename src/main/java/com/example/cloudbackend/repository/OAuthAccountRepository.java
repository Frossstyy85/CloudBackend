package com.example.cloudbackend.repository;

import com.example.cloudbackend.domain.OAuthAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OAuthAccountRepository extends JpaRepository<OAuthAccount ,Long> {

    Optional<OAuthAccount> findByProviderId(String providerId);

}
