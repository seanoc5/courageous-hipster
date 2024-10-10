package com.oconeco.courageous.service.impl;

import com.oconeco.courageous.domain.Content;
import com.oconeco.courageous.repository.ContentRepository;
import com.oconeco.courageous.service.ContentService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.oconeco.courageous.domain.Content}.
 */
@Service
@Transactional
public class ContentServiceImpl implements ContentService {

    private static final Logger LOG = LoggerFactory.getLogger(ContentServiceImpl.class);

    private final ContentRepository contentRepository;

    public ContentServiceImpl(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    @Override
    public Content save(Content content) {
        LOG.debug("Request to save Content : {}", content);
        return contentRepository.save(content);
    }

    @Override
    public Content update(Content content) {
        LOG.debug("Request to update Content : {}", content);
        return contentRepository.save(content);
    }

    @Override
    public Optional<Content> partialUpdate(Content content) {
        LOG.debug("Request to partially update Content : {}", content);

        return contentRepository
            .findById(content.getId())
            .map(existingContent -> {
                if (content.getTitle() != null) {
                    existingContent.setTitle(content.getTitle());
                }
                if (content.getUri() != null) {
                    existingContent.setUri(content.getUri());
                }
                if (content.getDescription() != null) {
                    existingContent.setDescription(content.getDescription());
                }
                if (content.getPath() != null) {
                    existingContent.setPath(content.getPath());
                }
                if (content.getSource() != null) {
                    existingContent.setSource(content.getSource());
                }
                if (content.getParams() != null) {
                    existingContent.setParams(content.getParams());
                }
                if (content.getBodyText() != null) {
                    existingContent.setBodyText(content.getBodyText());
                }
                if (content.getTextSize() != null) {
                    existingContent.setTextSize(content.getTextSize());
                }
                if (content.getStructuredContent() != null) {
                    existingContent.setStructuredContent(content.getStructuredContent());
                }
                if (content.getStructureSize() != null) {
                    existingContent.setStructureSize(content.getStructureSize());
                }
                if (content.getAuthor() != null) {
                    existingContent.setAuthor(content.getAuthor());
                }
                if (content.getLanguage() != null) {
                    existingContent.setLanguage(content.getLanguage());
                }
                if (content.getType() != null) {
                    existingContent.setType(content.getType());
                }
                if (content.getSubtype() != null) {
                    existingContent.setSubtype(content.getSubtype());
                }
                if (content.getMineType() != null) {
                    existingContent.setMineType(content.getMineType());
                }
                if (content.getPublishDate() != null) {
                    existingContent.setPublishDate(content.getPublishDate());
                }
                if (content.getOffensiveFlag() != null) {
                    existingContent.setOffensiveFlag(content.getOffensiveFlag());
                }
                if (content.getFavicon() != null) {
                    existingContent.setFavicon(content.getFavicon());
                }
                if (content.getDateCreated() != null) {
                    existingContent.setDateCreated(content.getDateCreated());
                }
                if (content.getLastUpdate() != null) {
                    existingContent.setLastUpdate(content.getLastUpdate());
                }

                return existingContent;
            })
            .map(contentRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Content> findAll() {
        LOG.debug("Request to get all Contents");
        return contentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Content> findOne(Long id) {
        LOG.debug("Request to get Content : {}", id);
        return contentRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Content : {}", id);
        contentRepository.deleteById(id);
    }
}
