package com.oconeco.courageous.service.impl;

import com.oconeco.courageous.domain.Context;
import com.oconeco.courageous.repository.ContextRepository;
import com.oconeco.courageous.service.ContextService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.oconeco.courageous.domain.Context}.
 */
@Service
@Transactional
public class ContextServiceImpl implements ContextService {

    private static final Logger LOG = LoggerFactory.getLogger(ContextServiceImpl.class);

    private final ContextRepository contextRepository;

    public ContextServiceImpl(ContextRepository contextRepository) {
        this.contextRepository = contextRepository;
    }

    @Override
    public Context save(Context context) {
        LOG.debug("Request to save Context : {}", context);
        return contextRepository.save(context);
    }

    @Override
    public Context update(Context context) {
        LOG.debug("Request to update Context : {}", context);
        return contextRepository.save(context);
    }

    @Override
    public Optional<Context> partialUpdate(Context context) {
        LOG.debug("Request to partially update Context : {}", context);

        return contextRepository
            .findById(context.getId())
            .map(existingContext -> {
                if (context.getLabel() != null) {
                    existingContext.setLabel(context.getLabel());
                }
                if (context.getDescription() != null) {
                    existingContext.setDescription(context.getDescription());
                }
                if (context.getLevel() != null) {
                    existingContext.setLevel(context.getLevel());
                }
                if (context.getTime() != null) {
                    existingContext.setTime(context.getTime());
                }
                if (context.getLocation() != null) {
                    existingContext.setLocation(context.getLocation());
                }
                if (context.getIntent() != null) {
                    existingContext.setIntent(context.getIntent());
                }
                if (context.getDefaultContext() != null) {
                    existingContext.setDefaultContext(context.getDefaultContext());
                }
                if (context.getDateCreated() != null) {
                    existingContext.setDateCreated(context.getDateCreated());
                }
                if (context.getLastUpdate() != null) {
                    existingContext.setLastUpdate(context.getLastUpdate());
                }

                return existingContext;
            })
            .map(contextRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Context> findAll() {
        LOG.debug("Request to get all Contexts");
        return contextRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Context> findOne(Long id) {
        LOG.debug("Request to get Context : {}", id);
        return contextRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Context : {}", id);
        contextRepository.deleteById(id);
    }
}
