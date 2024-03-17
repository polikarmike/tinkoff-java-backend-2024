package edu.java.scrapper.service.jdbc;

import edu.java.scrapper.domain.repository.jdbc.JDBCLinkRepository;
import edu.java.scrapper.dto.entity.Link;
import edu.java.scrapper.processor.LinkProcessor;
import edu.java.scrapper.service.LinkUpdater;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JDBCLinkUpdater implements LinkUpdater {
    private final JDBCLinkRepository jdbcLinkRepository;
    private final LinkProcessor linkProcessor;

    @Override
    public int update() {
        OffsetDateTime lastUpdateThreshold = OffsetDateTime.now().minusHours(1);
        List<Link> links = jdbcLinkRepository.findByLastUpdateBefore(lastUpdateThreshold);
        int updatedCount = 0;

        for (Link link : links) {
            linkProcessor.processLink(link);
            jdbcLinkRepository.updateLastUpdatedTime(link.getId());

            updatedCount++;
        }

        return updatedCount;
    }
}
