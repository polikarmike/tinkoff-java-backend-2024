package edu.java.scrapper.domain.repository;

import edu.java.scrapper.IntegrationEnvironment;
import edu.java.scrapper.dto.entity.Link;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LinkRepositoryTest extends IntegrationEnvironment {
    private static final Map<String, LinkRepository> linkRepositories = new HashMap<>();
    @Autowired
    @Qualifier("JOOQLinkRepository")
    private LinkRepository jooqlinkRepository;

    @Autowired
    @Qualifier("JDBCLinkRepository")
    private LinkRepository jdbclinkRepository;

    @BeforeAll
    void setup() {
        linkRepositories.put("JOOQ", jooqlinkRepository);
        linkRepositories.put("JDBC", jdbclinkRepository);
    }

    Stream<Arguments> implementations() {
        return Stream.of(
            Arguments.of("JOOQ", linkRepositories.get("JOOQ")),
            Arguments.of("JDBC", linkRepositories.get("JDBC"))
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("implementations")
    @DisplayName("Добавление ссылки")
    @Transactional
    @Rollback
    void addTest(String implName, LinkRepository linkRepository) throws URISyntaxException {
        URI exampleURI = new URI("https://example.com");

        Link addedLink = linkRepository.add(exampleURI);

        assertEquals(exampleURI, addedLink.getUri());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("implementations")
    @DisplayName("Удаление всех ссылок")
    @Transactional
    @Rollback
    void removeTest(String implName, LinkRepository linkRepository) throws URISyntaxException {
        URI exampleURI = new URI("https://example.com");
        Link addedLink = linkRepository.add(exampleURI);

        linkRepository.remove(exampleURI);

        assertTrue(linkRepository.getLinkById(addedLink.getId()).isEmpty());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("implementations")
    @DisplayName("Получение всех ссылок")
    @Transactional
    @Rollback
    void findAllTest(String implName, LinkRepository linkRepository) throws URISyntaxException {
        URI exampleURI1 = new URI("https://example1.com");
        Link addedLink1 = linkRepository.add(exampleURI1);

        URI exampleURI2 = new URI("https://example2.com");
        Link addedLink2 = linkRepository.add(exampleURI2);

        List<Link> links = linkRepository.findAll();

        assertEquals(2, links.size());

        assertTrue(links.contains(addedLink1));
        assertTrue(links.contains(addedLink2));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("implementations")
    @DisplayName("Получение ссылки по ID")
    @Transactional
    @Rollback
    void getLinkByIdTest(String implName, LinkRepository linkRepository) throws URISyntaxException {
        URI exampleURI = new URI("https://example.com");
        Link addedLink = linkRepository.add(exampleURI);

        Optional<Link> retrievedLink = linkRepository.getLinkById(addedLink.getId());

        assertTrue(retrievedLink.isPresent());
        assertEquals(addedLink, retrievedLink.get());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("implementations")
    @DisplayName("Получение ссылки по URI")
    @Transactional
    @Rollback
    void getLinkByUriTest(String implName, LinkRepository linkRepository) throws URISyntaxException {
        URI exampleURI = new URI("https://example.com");
        Link addedLink = linkRepository.add(exampleURI);

        Optional<Link> retrievedLink = linkRepository.getLinkByUri(exampleURI);

        assertTrue(retrievedLink.isPresent());
        assertEquals(addedLink, retrievedLink.get());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("implementations")
    @DisplayName("Обновление времени последней проверки ссылки")
    @Transactional
    @Rollback
    void updateLastUpdatedTimeTest(String implName, LinkRepository linkRepository) throws URISyntaxException {
        URI exampleURI = new URI("https://example.com");
        Link addedLink = linkRepository.add(exampleURI);

        OffsetDateTime initialLastUpdatedAt = addedLink.getLastUpdatedAt();

        linkRepository.updateLastUpdatedTime(addedLink.getId());

        Link updatedLink = linkRepository.getLinkById(addedLink.getId()).orElse(null);

        assertNotNull(updatedLink);
        assertTrue(initialLastUpdatedAt.isBefore(updatedLink.getLastUpdatedAt()));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("implementations")
    @DisplayName("Поиск старых ссылок")
    @Transactional
    @Rollback
    void findOldestLinksTest(String implName, LinkRepository linkRepository) throws URISyntaxException {
        int batchSize = 2;

        URI exampleURI1 = new URI("https://example1.com");
        Link addedLink1 = linkRepository.add(exampleURI1);

        URI exampleURI2 = new URI("https://example2.com");
        Link addedLink2 = linkRepository.add(exampleURI2);

        URI exampleURI3 = new URI("https://example3.com");
        Link addedLink3 = linkRepository.add(exampleURI3);

        List<Link> outdatedLinks = linkRepository.findOldestLinks(batchSize);

        assertTrue(outdatedLinks.contains(addedLink1));
        assertTrue(outdatedLinks.contains(addedLink2));
    }
}
