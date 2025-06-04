package com.example.cloudbackend.domain;

import jakarta.persistence.*;


@Entity
public class OAuth2Account {

    public OAuth2Account(){}

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String email;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    private String providerId;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    public User getUser(){
        return user;
    }

    public Provider getProvider(){
        return provider;
    }

    public OAuth2Account(String email, Provider provider, String providerId, User user) {
        this.email = email;
        this.provider = provider;
        this.providerId = providerId;
        this.user = user;
    }

}
