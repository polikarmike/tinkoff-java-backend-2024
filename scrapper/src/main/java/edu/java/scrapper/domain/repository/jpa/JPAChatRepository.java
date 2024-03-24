package edu.java.scrapper.domain.repository.jpa;

import edu.java.scrapper.dto.entity.jpa.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JPAChatRepository extends JpaRepository<Chat, Long> {

}
