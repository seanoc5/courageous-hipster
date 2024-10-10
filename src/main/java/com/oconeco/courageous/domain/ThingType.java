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
 * A ThingType.
 */
@Entity
@Table(name = "thing_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ThingType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "label", nullable = false)
    private String label;

    @Column(name = "parent_class")
    private String parentClass;

    @Column(name = "descrption")
    private String descrption;

    @Column(name = "date_created")
    private Instant dateCreated;

    @Column(name = "last_updated")
    private Instant lastUpdated;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "thingType")
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "thingType")
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

    public ThingType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return this.label;
    }

    public ThingType label(String label) {
        this.setLabel(label);
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getParentClass() {
        return this.parentClass;
    }

    public ThingType parentClass(String parentClass) {
        this.setParentClass(parentClass);
        return this;
    }

    public void setParentClass(String parentClass) {
        this.parentClass = parentClass;
    }

    public String getDescrption() {
        return this.descrption;
    }

    public ThingType descrption(String descrption) {
        this.setDescrption(descrption);
        return this;
    }

    public void setDescrption(String descrption) {
        this.descrption = descrption;
    }

    public Instant getDateCreated() {
        return this.dateCreated;
    }

    public ThingType dateCreated(Instant dateCreated) {
        this.setDateCreated(dateCreated);
        return this;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Instant getLastUpdated() {
        return this.lastUpdated;
    }

    public ThingType lastUpdated(Instant lastUpdated) {
        this.setLastUpdated(lastUpdated);
        return this;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Set<Tag> getTags() {
        return this.tags;
    }

    public void setTags(Set<Tag> tags) {
        if (this.tags != null) {
            this.tags.forEach(i -> i.setThingType(null));
        }
        if (tags != null) {
            tags.forEach(i -> i.setThingType(this));
        }
        this.tags = tags;
    }

    public ThingType tags(Set<Tag> tags) {
        this.setTags(tags);
        return this;
    }

    public ThingType addTag(Tag tag) {
        this.tags.add(tag);
        tag.setThingType(this);
        return this;
    }

    public ThingType removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.setThingType(null);
        return this;
    }

    public Set<Comment> getComments() {
        return this.comments;
    }

    public void setComments(Set<Comment> comments) {
        if (this.comments != null) {
            this.comments.forEach(i -> i.setThingType(null));
        }
        if (comments != null) {
            comments.forEach(i -> i.setThingType(this));
        }
        this.comments = comments;
    }

    public ThingType comments(Set<Comment> comments) {
        this.setComments(comments);
        return this;
    }

    public ThingType addComment(Comment comment) {
        this.comments.add(comment);
        comment.setThingType(this);
        return this;
    }

    public ThingType removeComment(Comment comment) {
        this.comments.remove(comment);
        comment.setThingType(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ThingType)) {
            return false;
        }
        return getId() != null && getId().equals(((ThingType) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ThingType{" +
            "id=" + getId() +
            ", label='" + getLabel() + "'" +
            ", parentClass='" + getParentClass() + "'" +
            ", descrption='" + getDescrption() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            "}";
    }
}
