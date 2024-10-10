package com.oconeco.courageous.service.impl;

import com.oconeco.courageous.domain.Analyzer;
import com.oconeco.courageous.repository.AnalyzerRepository;
import com.oconeco.courageous.service.AnalyzerService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.oconeco.courageous.domain.Analyzer}.
 */
@Service
@Transactional
public class AnalyzerServiceImpl implements AnalyzerService {

    private static final Logger LOG = LoggerFactory.getLogger(AnalyzerServiceImpl.class);

    private final AnalyzerRepository analyzerRepository;

    public AnalyzerServiceImpl(AnalyzerRepository analyzerRepository) {
        this.analyzerRepository = analyzerRepository;
    }

    @Override
    public Analyzer save(Analyzer analyzer) {
        LOG.debug("Request to save Analyzer : {}", analyzer);
        return analyzerRepository.save(analyzer);
    }

    @Override
    public Analyzer update(Analyzer analyzer) {
        LOG.debug("Request to update Analyzer : {}", analyzer);
        return analyzerRepository.save(analyzer);
    }

    @Override
    public Optional<Analyzer> partialUpdate(Analyzer analyzer) {
        LOG.debug("Request to partially update Analyzer : {}", analyzer);

        return analyzerRepository
            .findById(analyzer.getId())
            .map(existingAnalyzer -> {
                if (analyzer.getLabel() != null) {
                    existingAnalyzer.setLabel(analyzer.getLabel());
                }
                if (analyzer.getDescription() != null) {
                    existingAnalyzer.setDescription(analyzer.getDescription());
                }
                if (analyzer.getCode() != null) {
                    existingAnalyzer.setCode(analyzer.getCode());
                }
                if (analyzer.getDateCreated() != null) {
                    existingAnalyzer.setDateCreated(analyzer.getDateCreated());
                }
                if (analyzer.getLastUpdated() != null) {
                    existingAnalyzer.setLastUpdated(analyzer.getLastUpdated());
                }

                return existingAnalyzer;
            })
            .map(analyzerRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Analyzer> findAll() {
        LOG.debug("Request to get all Analyzers");
        return analyzerRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Analyzer> findOne(Long id) {
        LOG.debug("Request to get Analyzer : {}", id);
        return analyzerRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Analyzer : {}", id);
        analyzerRepository.deleteById(id);
    }
}
