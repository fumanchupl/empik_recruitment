package com.recruitment.githubconnector.repository;

import com.recruitment.githubconnector.domain.LoginCounter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginCounterRepository extends JpaRepository<LoginCounter, String> {
}
