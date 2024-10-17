package com.oconeco.courageous.service.impl;

import com.oconeco.courageous.domain.ThingType;
import com.oconeco.courageous.repository.ThingTypeRepository;
import com.oconeco.courageous.service.ThingTypeService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.oconeco.courageous.domain.ThingType}.
 */
@Service
@Transactional
public class ThingTypeServiceImpl implements ThingTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(ThingTypeServiceImpl.class);

    private final ThingTypeRepository thingTypeRepository;

    public ThingTypeServiceImpl(ThingTypeRepository thingTypeRepository) {
        this.thingTypeRepository = thingTypeRepository;
    }

    @Override
    public ThingType save(ThingType thingType) {
        LOG.debug("Request to save ThingType : {}", thingType);
        return thingTypeRepository.save(thingType);
    }

    @Override
    public ThingType update(ThingType thingType) {
        LOG.debug("Request to update ThingType : {}", thingType);
        return thingTypeRepository.save(thingType);
    }

    @Override
    public Optional<ThingType> partialUpdate(ThingType thingType) {
        LOG.debug("Request to partially update ThingType : {}", thingType);

        return thingTypeRepository
            .findById(thingType.getId())
            .map(existingThingType -> {
                if (thingType.getLabel() != null) {
                    existingThingType.setLabel(thingType.getLabel());
                }
                if (thingType.getParentClass() != null) {
                    existingThingType.setParentClass(thingType.getParentClass());
                }
                if (thingType.getDescrption() != null) {
                    existingThingType.setDescrption(thingType.getDescrption());
                }
                if (thingType.getDateCreated() != null) {
                    existingThingType.setDateCreated(thingType.getDateCreated());
                }
                if (thingType.getLastUpdated() != null) {
                    existingThingType.setLastUpdated(thingType.getLastUpdated());
                }

                return existingThingType;
            })
            .map(thingTypeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ThingType> findAll() {
        LOG.debug("Request to get all ThingTypes");
        return thingTypeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ThingType> findOne(Long id) {
        LOG.debug("Request to get ThingType : {}", id);
        return thingTypeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ThingType : {}", id);
        thingTypeRepository.deleteById(id);
    }
}
