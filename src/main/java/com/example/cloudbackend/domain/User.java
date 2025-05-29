        package com.example.cloudbackend.domain;

        import jakarta.persistence.*;
        import lombok.*;

        import java.util.ArrayList;
        import java.util.List;

        @Entity
        @Getter
        @Setter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        @ToString
        @Table(name = "users")
        public class User {

            @Id
            @GeneratedValue
            private Long id;

            private String name;
            private String email;

            @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
            @Builder.Default
            private List<OAuthAccount> linkedAccounts = new ArrayList<>();

            public void addOAuthAccount(OAuthAccount account){
                linkedAccounts.add(account);
                account.setUser(this);
            }



        }
