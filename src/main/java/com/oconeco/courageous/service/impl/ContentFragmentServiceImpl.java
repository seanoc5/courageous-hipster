package com.oconeco.courageous.service.impl;

import com.oconeco.courageous.domain.ContentFragment;
import com.oconeco.courageous.repository.ContentFragmentRepository;
import com.oconeco.courageous.service.ContentFragmentService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.oconeco.courageous.domain.ContentFragment}.
 */
@Service
@Transactional
public class ContentFragmentServiceImpl implements ContentFragmentService {

    private static final Logger LOG = LoggerFactory.getLogger(ContentFragmentServiceImpl.class);

    private final ContentFragmentRepository contentFragmentRepository;

    public ContentFragmentServiceImpl(ContentFragmentRepository contentFragmentRepository) {
        this.contentFragmentRepository = contentFragmentRepository;
    }

    @Override
    public ContentFragment save(ContentFragment contentFragment) {
        LOG.debug("Request to save ContentFragment : {}", contentFragment);
        return contentFragmentRepository.save(contentFragment);
    }

    @Override
    public ContentFragment update(ContentFragment contentFragment) {
        LOG.debug("Request to update ContentFragment : {}", contentFragment);
        return contentFragmentRepository.save(contentFragment);
    }

    @Override
    public Optional<ContentFragment> partialUpdate(ContentFragment contentFragment) {
        LOG.debug("Request to partially update ContentFragment : {}", contentFragment);

        return contentFragmentRepository
            .findById(contentFragment.getId())
            .map(existingContentFragment -> {
                if (contentFragment.getLabel() != null) {
                    existingContentFragment.setLabel(contentFragment.getLabel());
                }
                if (contentFragment.getText() != null) {
                    existingContentFragment.setText(contentFragment.getText());
                }
                if (contentFragment.getDescription() != null) {
                    existingContentFragment.setDescription(contentFragment.getDescription());
                }
                if (contentFragment.getStructuredContent() != null) {
                    existingContentFragment.setStructuredContent(contentFragment.getStructuredContent());
                }
                if (contentFragment.getStartPos() != null) {
                    existingContentFragment.setStartPos(contentFragment.getStartPos());
                }
                if (contentFragment.getEndPos() != null) {
                    existingContentFragment.setEndPos(contentFragment.getEndPos());
                }
                if (contentFragment.getStartTermNum() != null) {
                    existingContentFragment.setStartTermNum(contentFragment.getStartTermNum());
                }
                if (contentFragment.getEndTermNum() != null) {
                    existingContentFragment.setEndTermNum(contentFragment.getEndTermNum());
                }
                if (contentFragment.getSubtype() != null) {
                    existingContentFragment.setSubtype(contentFragment.getSubtype());
                }
                if (contentFragment.getLanguage() != null) {
                    existingContentFragment.setLanguage(contentFragment.getLanguage());
                }
                if (contentFragment.getDateCreated() != null) {
                    existingContentFragment.setDateCreated(contentFragment.getDateCreated());
                }
                if (contentFragment.getLastUpdated() != null) {
                    existingContentFragment.setLastUpdated(contentFragment.getLastUpdated());
                }

                return existingContentFragment;
            })
            .map(contentFragmentRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContentFragment> findAll() {
        LOG.debug("Request to get all ContentFragments");
        return contentFragmentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ContentFragment> findOne(Long id) {
        LOG.debug("Request to get ContentFragment : {}", id);
        return contentFragmentRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ContentFragment : {}", id);
        contentFragmentRepository.deleteById(id);
    }
}
