package com.e_commerce.repository;

import com.e_commerce.entity.ContactMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactMessageRepo extends JpaRepository<ContactMessage, Long> {
}
