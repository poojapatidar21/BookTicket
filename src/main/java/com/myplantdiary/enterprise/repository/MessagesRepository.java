package com.myplantdiary.enterprise.repository;
import com.myplantdiary.enterprise.entity.Messages;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessagesRepository extends JpaRepository<Messages, Long> {
}
