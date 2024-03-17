package edu.java.scrapper.service.jdbc;

import edu.java.scrapper.dao.repository.LinkDAO;
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
    private final LinkDAO linkRepository;
    private final LinkProcessor linkProcessor;

    @Override
    public int update() {
        OffsetDateTime lastUpdateThreshold = OffsetDateTime.now().minusHours(1);
        List<Link> links = linkRepository.findByLastUpdateBefore(lastUpdateThreshold);
        int updatedCount = 0;

        for (Link link : links) {
            linkProcessor.processLink(link);
            linkRepository.updateLastUpdatedTime(link.getId());

            updatedCount++;
        }

        return updatedCount;
    }
}
