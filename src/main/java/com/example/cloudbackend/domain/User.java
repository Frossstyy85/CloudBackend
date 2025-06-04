package com.example.cloudbackend.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    public User(){

    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String email;

    public String getEmail(){
        return email;
    }

    public String name(){
        return name;
    }

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private final List<OAuth2Account> linkedAccounts = new ArrayList<>();

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public User linkOAuthAccount(OAuth2Account newAccount) {
        boolean alreadyLinked = linkedAccounts.stream()
                .anyMatch(account -> account.getProvider().equals(newAccount.getProvider()));
        if (!alreadyLinked) {
            linkedAccounts.add(newAccount);
        }
        return this;
    }

    public User unlinkOAuthAccount(Provider provider) {
        Iterator<OAuth2Account> iterator = linkedAccounts.iterator();
        while (iterator.hasNext()) {
            var account = iterator.next();
            if (account.getProvider().equals(provider)) {
                iterator.remove();
                break;
            }
        }
        return this;
    }
}
