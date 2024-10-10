package com.oconeco.courageous.service.impl;

import com.oconeco.courageous.domain.Tag;
import com.oconeco.courageous.repository.TagRepository;
import com.oconeco.courageous.service.TagService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.oconeco.courageous.domain.Tag}.
 */
@Service
@Transactional
public class TagServiceImpl implements TagService {

    private static final Logger LOG = LoggerFactory.getLogger(TagServiceImpl.class);

    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public Tag save(Tag tag) {
        LOG.debug("Request to save Tag : {}", tag);
        return tagRepository.save(tag);
    }

    @Override
    public Tag update(Tag tag) {
        LOG.debug("Request to update Tag : {}", tag);
        return tagRepository.save(tag);
    }

    @Override
    public Optional<Tag> partialUpdate(Tag tag) {
        LOG.debug("Request to partially update Tag : {}", tag);

        return tagRepository
            .findById(tag.getId())
            .map(existingTag -> {
                if (tag.getLabel() != null) {
                    existingTag.setLabel(tag.getLabel());
                }
                if (tag.getDescription() != null) {
                    existingTag.setDescription(tag.getDescription());
                }
                if (tag.getDefaultTag() != null) {
                    existingTag.setDefaultTag(tag.getDefaultTag());
                }
                if (tag.getDateCreated() != null) {
                    existingTag.setDateCreated(tag.getDateCreated());
                }
                if (tag.getLastUpdated() != null) {
                    existingTag.setLastUpdated(tag.getLastUpdated());
                }

                return existingTag;
            })
            .map(tagRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tag> findAll() {
        LOG.debug("Request to get all Tags");
        return tagRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Tag> findOne(Long id) {
        LOG.debug("Request to get Tag : {}", id);
        return tagRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Tag : {}", id);
        tagRepository.deleteById(id);
    }
}
