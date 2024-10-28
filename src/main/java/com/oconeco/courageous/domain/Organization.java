package com.oconeco.courageous.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Organization.
 */
@Entity
@Table(name = "organization")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Organization implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "url")
    private String url;

    @Column(name = "description")
    private String description;

    @Column(name = "date_created")
    private Instant dateCreated;

    @Column(name = "last_updated")
    private Instant lastUpdated;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organization")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "analyzers", "tags", "contentFragments", "comments", "createdBy", "organization" }, allowSetters = true)
    private Set<Context> contexts = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organization")
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
            "search",
            "searchConfiguration",
            "searchResult",
            "thingType",
            "topic",
        },
        allowSetters = true
    )
    private Set<Tag> tags = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organization")
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

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Organization id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Organization name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return this.url;
    }

    public Organization url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return this.description;
    }

    public Organization description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getDateCreated() {
        return this.dateCreated;
    }

    public Organization dateCreated(Instant dateCreated) {
        this.setDateCreated(dateCreated);
        return this;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Instant getLastUpdated() {
        return this.lastUpdated;
    }

    public Organization lastUpdated(Instant lastUpdated) {
        this.setLastUpdated(lastUpdated);
        return this;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Set<Context> getContexts() {
        return this.contexts;
    }

    public void setContexts(Set<Context> contexts) {
        if (this.contexts != null) {
            this.contexts.forEach(i -> i.setOrganization(null));
        }
        if (contexts != null) {
            contexts.forEach(i -> i.setOrganization(this));
        }
        this.contexts = contexts;
    }

    public Organization contexts(Set<Context> contexts) {
        this.setContexts(contexts);
        return this;
    }

    public Organization addContext(Context context) {
        this.contexts.add(context);
        context.setOrganization(this);
        return this;
    }

    public Organization removeContext(Context context) {
        this.contexts.remove(context);
        context.setOrganization(null);
        return this;
    }

    public Set<Tag> getTags() {
        return this.tags;
    }

    public void setTags(Set<Tag> tags) {
        if (this.tags != null) {
            this.tags.forEach(i -> i.setOrganization(null));
        }
        if (tags != null) {
            tags.forEach(i -> i.setOrganization(this));
        }
        this.tags = tags;
    }

    public Organization tags(Set<Tag> tags) {
        this.setTags(tags);
        return this;
    }

    public Organization addTag(Tag tag) {
        this.tags.add(tag);
        tag.setOrganization(this);
        return this;
    }

    public Organization removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.setOrganization(null);
        return this;
    }

    public Set<Comment> getComments() {
        return this.comments;
    }

    public void setComments(Set<Comment> comments) {
        if (this.comments != null) {
            this.comments.forEach(i -> i.setOrganization(null));
        }
        if (comments != null) {
            comments.forEach(i -> i.setOrganization(this));
        }
        this.comments = comments;
    }

    public Organization comments(Set<Comment> comments) {
        this.setComments(comments);
        return this;
    }

    public Organization addComment(Comment comment) {
        this.comments.add(comment);
        comment.setOrganization(this);
        return this;
    }

    public Organization removeComment(Comment comment) {
        this.comments.remove(comment);
        comment.setOrganization(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Organization)) {
            return false;
        }
        return getId() != null && getId().equals(((Organization) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Organization{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", url='" + getUrl() + "'" +
            ", description='" + getDescription() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            "}";
    }
}
