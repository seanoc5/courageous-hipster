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
 * A Context.
 */
@Entity
@Table(name = "context")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Context implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "label", nullable = false)
    private String label;

    @Column(name = "description")
    private String description;

    @Column(name = "level")
    private String level;

    @Column(name = "time")
    private String time;

    @Column(name = "location")
    private String location;

    @Column(name = "intent")
    private String intent;

    @Column(name = "default_context")
    private Boolean defaultContext;

    @Column(name = "date_created")
    private Instant dateCreated;

    @Column(name = "last_update")
    private Instant lastUpdate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "context")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tags", "createdBy", "context", "searchConfiguration", "searchResult" }, allowSetters = true)
    private Set<Analyzer> analyzers = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "context")
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "context")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tags", "comments", "type", "content", "context", "topic" }, allowSetters = true)
    private Set<ContentFragment> contentFragments = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "context")
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

    @ManyToOne(fetch = FetchType.LAZY)
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "contexts", "tags", "comments" }, allowSetters = true)
    private Organization organization;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Context id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return this.label;
    }

    public Context label(String label) {
        this.setLabel(label);
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return this.description;
    }

    public Context description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLevel() {
        return this.level;
    }

    public Context level(String level) {
        this.setLevel(level);
        return this;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getTime() {
        return this.time;
    }

    public Context time(String time) {
        this.setTime(time);
        return this;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return this.location;
    }

    public Context location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIntent() {
        return this.intent;
    }

    public Context intent(String intent) {
        this.setIntent(intent);
        return this;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public Boolean getDefaultContext() {
        return this.defaultContext;
    }

    public Context defaultContext(Boolean defaultContext) {
        this.setDefaultContext(defaultContext);
        return this;
    }

    public void setDefaultContext(Boolean defaultContext) {
        this.defaultContext = defaultContext;
    }

    public Instant getDateCreated() {
        return this.dateCreated;
    }

    public Context dateCreated(Instant dateCreated) {
        this.setDateCreated(dateCreated);
        return this;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Instant getLastUpdate() {
        return this.lastUpdate;
    }

    public Context lastUpdate(Instant lastUpdate) {
        this.setLastUpdate(lastUpdate);
        return this;
    }

    public void setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Set<Analyzer> getAnalyzers() {
        return this.analyzers;
    }

    public void setAnalyzers(Set<Analyzer> analyzers) {
        if (this.analyzers != null) {
            this.analyzers.forEach(i -> i.setContext(null));
        }
        if (analyzers != null) {
            analyzers.forEach(i -> i.setContext(this));
        }
        this.analyzers = analyzers;
    }

    public Context analyzers(Set<Analyzer> analyzers) {
        this.setAnalyzers(analyzers);
        return this;
    }

    public Context addAnalyzer(Analyzer analyzer) {
        this.analyzers.add(analyzer);
        analyzer.setContext(this);
        return this;
    }

    public Context removeAnalyzer(Analyzer analyzer) {
        this.analyzers.remove(analyzer);
        analyzer.setContext(null);
        return this;
    }

    public Set<Tag> getTags() {
        return this.tags;
    }

    public void setTags(Set<Tag> tags) {
        if (this.tags != null) {
            this.tags.forEach(i -> i.setContext(null));
        }
        if (tags != null) {
            tags.forEach(i -> i.setContext(this));
        }
        this.tags = tags;
    }

    public Context tags(Set<Tag> tags) {
        this.setTags(tags);
        return this;
    }

    public Context addTag(Tag tag) {
        this.tags.add(tag);
        tag.setContext(this);
        return this;
    }

    public Context removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.setContext(null);
        return this;
    }

    public Set<ContentFragment> getContentFragments() {
        return this.contentFragments;
    }

    public void setContentFragments(Set<ContentFragment> contentFragments) {
        if (this.contentFragments != null) {
            this.contentFragments.forEach(i -> i.setContext(null));
        }
        if (contentFragments != null) {
            contentFragments.forEach(i -> i.setContext(this));
        }
        this.contentFragments = contentFragments;
    }

    public Context contentFragments(Set<ContentFragment> contentFragments) {
        this.setContentFragments(contentFragments);
        return this;
    }

    public Context addContentFragment(ContentFragment contentFragment) {
        this.contentFragments.add(contentFragment);
        contentFragment.setContext(this);
        return this;
    }

    public Context removeContentFragment(ContentFragment contentFragment) {
        this.contentFragments.remove(contentFragment);
        contentFragment.setContext(null);
        return this;
    }

    public Set<Comment> getComments() {
        return this.comments;
    }

    public void setComments(Set<Comment> comments) {
        if (this.comments != null) {
            this.comments.forEach(i -> i.setContext(null));
        }
        if (comments != null) {
            comments.forEach(i -> i.setContext(this));
        }
        this.comments = comments;
    }

    public Context comments(Set<Comment> comments) {
        this.setComments(comments);
        return this;
    }

    public Context addComment(Comment comment) {
        this.comments.add(comment);
        comment.setContext(this);
        return this;
    }

    public Context removeComment(Comment comment) {
        this.comments.remove(comment);
        comment.setContext(null);
        return this;
    }

    public User getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(User user) {
        this.createdBy = user;
    }

    public Context createdBy(User user) {
        this.setCreatedBy(user);
        return this;
    }

    public Organization getOrganization() {
        return this.organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Context organization(Organization organization) {
        this.setOrganization(organization);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Context)) {
            return false;
        }
        return getId() != null && getId().equals(((Context) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Context{" +
            "id=" + getId() +
            ", label='" + getLabel() + "'" +
            ", description='" + getDescription() + "'" +
            ", level='" + getLevel() + "'" +
            ", time='" + getTime() + "'" +
            ", location='" + getLocation() + "'" +
            ", intent='" + getIntent() + "'" +
            ", defaultContext='" + getDefaultContext() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", lastUpdate='" + getLastUpdate() + "'" +
            "}";
    }
}
