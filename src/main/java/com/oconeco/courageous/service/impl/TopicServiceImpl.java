package com.oconeco.courageous.service.impl;

import com.oconeco.courageous.domain.Topic;
import com.oconeco.courageous.repository.TopicRepository;
import com.oconeco.courageous.service.TopicService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.oconeco.courageous.domain.Topic}.
 */
@Service
@Transactional
public class TopicServiceImpl implements TopicService {

    private static final Logger LOG = LoggerFactory.getLogger(TopicServiceImpl.class);

    private final TopicRepository topicRepository;

    public TopicServiceImpl(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    @Override
    public Topic save(Topic topic) {
        LOG.debug("Request to save Topic : {}", topic);
        return topicRepository.save(topic);
    }

    @Override
    public Topic update(Topic topic) {
        LOG.debug("Request to update Topic : {}", topic);
        return topicRepository.save(topic);
    }

    @Override
    public Optional<Topic> partialUpdate(Topic topic) {
        LOG.debug("Request to partially update Topic : {}", topic);

        return topicRepository
            .findById(topic.getId())
            .map(existingTopic -> {
                if (topic.getLabel() != null) {
                    existingTopic.setLabel(topic.getLabel());
                }
                if (topic.getDescription() != null) {
                    existingTopic.setDescription(topic.getDescription());
                }
                if (topic.getDefaultTopic() != null) {
                    existingTopic.setDefaultTopic(topic.getDefaultTopic());
                }
                if (topic.getLevel() != null) {
                    existingTopic.setLevel(topic.getLevel());
                }
                if (topic.getDateCreated() != null) {
                    existingTopic.setDateCreated(topic.getDateCreated());
                }
                if (topic.getLastUpdated() != null) {
                    existingTopic.setLastUpdated(topic.getLastUpdated());
                }

                return existingTopic;
            })
            .map(topicRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Topic> findAll() {
        LOG.debug("Request to get all Topics");
        return topicRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Topic> findOne(Long id) {
        LOG.debug("Request to get Topic : {}", id);
        return topicRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Topic : {}", id);
        topicRepository.deleteById(id);
    }
}
