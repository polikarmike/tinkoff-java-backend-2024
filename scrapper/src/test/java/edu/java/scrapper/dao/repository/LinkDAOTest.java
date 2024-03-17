package edu.java.scrapper.dao.repository;//package edu.java.scrapper.dao.repository;

import edu.java.scrapper.IntegrationEnvironment;
import edu.java.scrapper.dto.entity.Link;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class LinkDAOTest extends IntegrationEnvironment {

    @Autowired
    private LinkDAO linkRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Test
    @DisplayName("Добавление ссылки")
    @Transactional
    @Rollback
    void addTest() throws URISyntaxException {
        URI exampleURI = new URI("https://example.com");

        linkRepository.add(exampleURI);

        assertEquals(1, jdbcTemplate.queryForObject("SELECT COUNT(*) FROM link", Long.class));
    }

    @Test
    @DisplayName("Удаление ссылки")
    @Transactional
    @Rollback
    void removeTest() throws URISyntaxException {
        URI exampleURI = new URI("https://example.com");
        linkRepository.add(exampleURI);

        assertEquals(1, jdbcTemplate.queryForObject("SELECT COUNT(*) FROM Link WHERE url = ?", Long.class, exampleURI.toString()));

        linkRepository.remove(exampleURI);

        assertEquals(0, jdbcTemplate.queryForObject("SELECT COUNT(*) FROM Link WHERE url = ?", Long.class, exampleURI.toString()));
    }

    @Test
    @DisplayName("Получение всех ссылок")
    @Transactional
    @Rollback
    void findAllTest() throws URISyntaxException {
        URI exampleURI = new URI("https://example.com");
        linkRepository.add(exampleURI);

        URI exampleURI2 = new URI("https://example2.com");
        linkRepository.add(exampleURI2);

        List<Link> links = linkRepository.findAll();

        assertEquals(2, links.size());

        assertTrue(links.stream().anyMatch(link -> link.getUri().equals(exampleURI)));
        assertTrue(links.stream().anyMatch(link -> link.getUri().equals(exampleURI2)));
    }

    @Test
    @DisplayName("Получение ссылки по ID")
    @Transactional
    @Rollback
    void getLinkByIdTest() throws URISyntaxException {
        URI exampleURI = new URI("https://example.com");
        Link addedLink = linkRepository.add(exampleURI);

        Optional<Link> retrievedLink = linkRepository.getLinkById(addedLink.getId());

        assertTrue(retrievedLink.isPresent());
        assertEquals(addedLink, retrievedLink.get());
    }

    @Test
    @DisplayName("Получение ссылки по URI")
    @Transactional
    @Rollback
    void getLinkByUriTest() throws URISyntaxException {
        URI exampleURI = new URI("https://example.com");
        Link addedLink = linkRepository.add(exampleURI);

        Optional<Link> retrievedLink = linkRepository.getLinkByUri(exampleURI);

        assertTrue(retrievedLink.isPresent());
        assertEquals(addedLink, retrievedLink.get());
    }

    @Test
    @DisplayName("Обновление времени последней проверки ссылки")
    @Transactional
    @Rollback
    void updateLastUpdatedTimeTest() throws URISyntaxException {
        URI exampleURI = new URI("https://example.com");
        Link addedLink = linkRepository.add(exampleURI);

        OffsetDateTime initialLastUpdatedAt = addedLink.getLastUpdatedAt();

        linkRepository.updateLastUpdatedTime(addedLink.getId());

        Link updatedLink = linkRepository.getLinkById(addedLink.getId()).orElse(null);

        assertNotNull(updatedLink);
        assertTrue(initialLastUpdatedAt.isBefore(updatedLink.getLastUpdatedAt()));
    }

    @Test
    @DisplayName("Поиск устаревших ссылок")
    @Transactional
    @Rollback
    void findByLastUpdateBeforeTest() throws URISyntaxException {
        URI exampleURI = new URI("https://example.com");
        Link addedLink = linkRepository.add(exampleURI);

        OffsetDateTime thresholdTime = OffsetDateTime.now().minusDays(1);

        List<Link> outdatedLinks = linkRepository.findByLastUpdateBefore(thresholdTime);

        assertTrue(outdatedLinks.isEmpty());

        outdatedLinks = linkRepository.findByLastUpdateBefore(OffsetDateTime.now());

        Assertions.assertFalse(outdatedLinks.isEmpty());
        assertTrue(outdatedLinks.contains(addedLink));
    }
}
