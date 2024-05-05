package com.pay1oad.homepage.model;

import com.pay1oad.homepage.model.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userid;

    @Column(nullable = false)
    private String username;

    private String passwd;

    private String email;

    private String role;

    private String authProvider;

    private Boolean verified;

    @OneToMany(mappedBy = "member")
    private List<Post> posts;
}
