package com.pay1oad.homepage.persistence;

import com.pay1oad.homepage.model.EmailVerification;
import com.pay1oad.homepage.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, String> {


}
