package com.oconeco.courageous.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Topic.
 */
@Entity
@Table(name = "topic")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Topic implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "label")
    private String label;

    @Column(name = "description")
    private String description;

    @Column(name = "default_topic")
    private Boolean defaultTopic;

    @Column(name = "level")
    private Integer level;

    @Column(name = "date_created")
    private Instant dateCreated;

    @Column(name = "last_updated")
    private Instant lastUpdated;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "topic")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "user",
            "content",
            "contentFragment",
            "context",
            "organization",
            "search",
            "searchConfiguration",
            "searchResult",
            "tag",
            "thingType",
            "topic",
        },
        allowSetters = true
    )
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "topic")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tags", "comments", "type", "content", "context", "topic" }, allowSetters = true)
    private Set<ContentFragment> contentFragments = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "topic")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "comments",
            "createdBy",
            "analyzer",
            "content",
            "contentFragment",
            "context",
            "organization",
            "searchConfiguration",
            "searchResult",
            "thingType",
            "topic",
            "searches",
        },
        allowSetters = true
    )
    private Set<Tag> tags = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private User createdBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Topic id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return this.label;
    }

    public Topic label(String label) {
        this.setLabel(label);
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return this.description;
    }

    public Topic description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getDefaultTopic() {
        return this.defaultTopic;
    }

    public Topic defaultTopic(Boolean defaultTopic) {
        this.setDefaultTopic(defaultTopic);
        return this;
    }

    public void setDefaultTopic(Boolean defaultTopic) {
        this.defaultTopic = defaultTopic;
    }

    public Integer getLevel() {
        return this.level;
    }

    public Topic level(Integer level) {
        this.setLevel(level);
        return this;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Instant getDateCreated() {
        return this.dateCreated;
    }

    public Topic dateCreated(Instant dateCreated) {
        this.setDateCreated(dateCreated);
        return this;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Instant getLastUpdated() {
        return this.lastUpdated;
    }

    public Topic lastUpdated(Instant lastUpdated) {
        this.setLastUpdated(lastUpdated);
        return this;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Set<Comment> getComments() {
        return this.comments;
    }

    public void setComments(Set<Comment> comments) {
        if (this.comments != null) {
            this.comments.forEach(i -> i.setTopic(null));
        }
        if (comments != null) {
            comments.forEach(i -> i.setTopic(this));
        }
        this.comments = comments;
    }

    public Topic comments(Set<Comment> comments) {
        this.setComments(comments);
        return this;
    }

    public Topic addComment(Comment comment) {
        this.comments.add(comment);
        comment.setTopic(this);
        return this;
    }

    public Topic removeComment(Comment comment) {
        this.comments.remove(comment);
        comment.setTopic(null);
        return this;
    }

    public Set<ContentFragment> getContentFragments() {
        return this.contentFragments;
    }

    public void setContentFragments(Set<ContentFragment> contentFragments) {
        if (this.contentFragments != null) {
            this.contentFragments.forEach(i -> i.setTopic(null));
        }
        if (contentFragments != null) {
            contentFragments.forEach(i -> i.setTopic(this));
        }
        this.contentFragments = contentFragments;
    }

    public Topic contentFragments(Set<ContentFragment> contentFragments) {
        this.setContentFragments(contentFragments);
        return this;
    }

    public Topic addContentFragment(ContentFragment contentFragment) {
        this.contentFragments.add(contentFragment);
        contentFragment.setTopic(this);
        return this;
    }

    public Topic removeContentFragment(ContentFragment contentFragment) {
        this.contentFragments.remove(contentFragment);
        contentFragment.setTopic(null);
        return this;
    }

    public Set<Tag> getTags() {
        return this.tags;
    }

    public void setTags(Set<Tag> tags) {
        if (this.tags != null) {
            this.tags.forEach(i -> i.setTopic(null));
        }
        if (tags != null) {
            tags.forEach(i -> i.setTopic(this));
        }
        this.tags = tags;
    }

    public Topic tags(Set<Tag> tags) {
        this.setTags(tags);
        return this;
    }

    public Topic addTag(Tag tag) {
        this.tags.add(tag);
        tag.setTopic(this);
        return this;
    }

    public Topic removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.setTopic(null);
        return this;
    }

    public User getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(User user) {
        this.createdBy = user;
    }

    public Topic createdBy(User user) {
        this.setCreatedBy(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Topic)) {
            return false;
        }
        return getId() != null && getId().equals(((Topic) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Topic{" +
            "id=" + getId() +
            ", label='" + getLabel() + "'" +
            ", description='" + getDescription() + "'" +
            ", defaultTopic='" + getDefaultTopic() + "'" +
            ", level=" + getLevel() +
            ", dateCreated='" + getDateCreated() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            "}";
    }
}
