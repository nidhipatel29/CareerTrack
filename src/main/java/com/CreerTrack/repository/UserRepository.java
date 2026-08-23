package com.CreerTrack.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.CreerTrack.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}