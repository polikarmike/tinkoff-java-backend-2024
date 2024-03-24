package edu.java.scrapper.domain.repository.jpa;

import edu.java.scrapper.dto.entity.jpa.Link;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JPALinkRepository extends JpaRepository<Link, Long> {
    Optional<Link> findByUri(URI uri);

    @Query(value = "SELECT COUNT(*) > 0 FROM Link_Chat WHERE chat_id = ? AND link_id = ?", nativeQuery = true)
    boolean exists(@Param("chat_id") long chatId, @Param("link_id") long linkId);

    @Query(value = "SELECT * FROM Link ORDER BY last_updated_at ASC LIMIT ?", nativeQuery = true)
    List<Link> findOldestLinksWithLimit(@Param("limit") int limit);
}
