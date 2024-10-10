package com.oconeco.courageous.service.impl;

import com.oconeco.courageous.domain.Comment;
import com.oconeco.courageous.repository.CommentRepository;
import com.oconeco.courageous.service.CommentService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.oconeco.courageous.domain.Comment}.
 */
@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    private static final Logger LOG = LoggerFactory.getLogger(CommentServiceImpl.class);

    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment save(Comment comment) {
        LOG.debug("Request to save Comment : {}", comment);
        return commentRepository.save(comment);
    }

    @Override
    public Comment update(Comment comment) {
        LOG.debug("Request to update Comment : {}", comment);
        return commentRepository.save(comment);
    }

    @Override
    public Optional<Comment> partialUpdate(Comment comment) {
        LOG.debug("Request to partially update Comment : {}", comment);

        return commentRepository
            .findById(comment.getId())
            .map(existingComment -> {
                if (comment.getLabel() != null) {
                    existingComment.setLabel(comment.getLabel());
                }
                if (comment.getDescription() != null) {
                    existingComment.setDescription(comment.getDescription());
                }
                if (comment.getUrl() != null) {
                    existingComment.setUrl(comment.getUrl());
                }
                if (comment.getDateCreated() != null) {
                    existingComment.setDateCreated(comment.getDateCreated());
                }
                if (comment.getLastUpdate() != null) {
                    existingComment.setLastUpdate(comment.getLastUpdate());
                }

                return existingComment;
            })
            .map(commentRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> findAll() {
        LOG.debug("Request to get all Comments");
        return commentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Comment> findOne(Long id) {
        LOG.debug("Request to get Comment : {}", id);
        return commentRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Comment : {}", id);
        commentRepository.deleteById(id);
    }
}
