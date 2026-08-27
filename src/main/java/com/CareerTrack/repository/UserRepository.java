package com.CareerTrack.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.CareerTrack.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}