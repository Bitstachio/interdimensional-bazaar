package com.bitstachio.bazaarapi.user.repository;

import com.bitstachio.bazaarapi.user.domain.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {}
