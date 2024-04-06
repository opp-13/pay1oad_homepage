package com.pay1oad.homepage.persistence;


import com.pay1oad.homepage.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {
    Member findByUsername(String username);
    Boolean existsByUsername(String username);
    Member findByUsernameAndPasswd(String username, String passwd);
}
