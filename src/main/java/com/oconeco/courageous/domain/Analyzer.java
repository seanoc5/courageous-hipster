package com.oconeco.courageous.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Analyzer.
 */
@Entity
@Table(name = "analyzer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Analyzer implements Serializable {

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

    /**
     * closure type groovy code to be run)
     */
    @Schema(description = "closure type groovy code to be run)")
    @Column(name = "code")
    private String code;

    @Column(name = "date_created")
    private Instant dateCreated;

    @Column(name = "last_updated")
    private Instant lastUpdated;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "analyzer")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "analyzers", "tags", "contentFragments", "comments", "createdBy", "organization" }, allowSetters = true)
    private Context context;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tags", "comments", "analyzers", "createdBy", "searches" }, allowSetters = true)
    private SearchConfiguration searchConfiguration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "contents", "tags", "comments", "analyzers", "config", "search" }, allowSetters = true)
    private SearchResult searchResult;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Analyzer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return this.label;
    }

    public Analyzer label(String label) {
        this.setLabel(label);
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return this.description;
    }

    public Analyzer description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return this.code;
    }

    public Analyzer code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Instant getDateCreated() {
        return this.dateCreated;
    }

    public Analyzer dateCreated(Instant dateCreated) {
        this.setDateCreated(dateCreated);
        return this;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Instant getLastUpdated() {
        return this.lastUpdated;
    }

    public Analyzer lastUpdated(Instant lastUpdated) {
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
            this.tags.forEach(i -> i.setAnalyzer(null));
        }
        if (tags != null) {
            tags.forEach(i -> i.setAnalyzer(this));
        }
        this.tags = tags;
    }

    public Analyzer tags(Set<Tag> tags) {
        this.setTags(tags);
        return this;
    }

    public Analyzer addTag(Tag tag) {
        this.tags.add(tag);
        tag.setAnalyzer(this);
        return this;
    }

    public Analyzer removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.setAnalyzer(null);
        return this;
    }

    public User getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(User user) {
        this.createdBy = user;
    }

    public Analyzer createdBy(User user) {
        this.setCreatedBy(user);
        return this;
    }

    public Context getContext() {
        return this.context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Analyzer context(Context context) {
        this.setContext(context);
        return this;
    }

    public SearchConfiguration getSearchConfiguration() {
        return this.searchConfiguration;
    }

    public void setSearchConfiguration(SearchConfiguration searchConfiguration) {
        this.searchConfiguration = searchConfiguration;
    }

    public Analyzer searchConfiguration(SearchConfiguration searchConfiguration) {
        this.setSearchConfiguration(searchConfiguration);
        return this;
    }

    public SearchResult getSearchResult() {
        return this.searchResult;
    }

    public void setSearchResult(SearchResult searchResult) {
        this.searchResult = searchResult;
    }

    public Analyzer searchResult(SearchResult searchResult) {
        this.setSearchResult(searchResult);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Analyzer)) {
            return false;
        }
        return getId() != null && getId().equals(((Analyzer) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Analyzer{" +
            "id=" + getId() +
            ", label='" + getLabel() + "'" +
            ", description='" + getDescription() + "'" +
            ", code='" + getCode() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            "}";
    }
}
