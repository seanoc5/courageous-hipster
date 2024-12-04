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
 * A SearchConfiguration.
 */
@Entity
@Table(name = "search_configuration")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SearchConfiguration implements Serializable {

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

    @Column(name = "default_config")
    private Boolean defaultConfig;

    @Column(name = "url")
    private String url;

    @Column(name = "params_json")
    private String paramsJson;

    @Column(name = "headers_json")
    private String headersJson;

    @Column(name = "date_created")
    private Instant dateCreated;

    @Column(name = "last_updated")
    private Instant lastUpdated;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "searchConfiguration")
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "searchConfiguration")
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "searchConfiguration")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tags", "createdBy", "context", "searchConfiguration", "searchResult" }, allowSetters = true)
    private Set<Analyzer> analyzers = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private User createdBy;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "configurations")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "searchResults", "tags", "comments", "configurations", "createdBy", "context", "type" },
        allowSetters = true
    )
    private Set<Search> searches = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SearchConfiguration id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return this.label;
    }

    public SearchConfiguration label(String label) {
        this.setLabel(label);
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return this.description;
    }

    public SearchConfiguration description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getDefaultConfig() {
        return this.defaultConfig;
    }

    public SearchConfiguration defaultConfig(Boolean defaultConfig) {
        this.setDefaultConfig(defaultConfig);
        return this;
    }

    public void setDefaultConfig(Boolean defaultConfig) {
        this.defaultConfig = defaultConfig;
    }

    public String getUrl() {
        return this.url;
    }

    public SearchConfiguration url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParamsJson() {
        return this.paramsJson;
    }

    public SearchConfiguration paramsJson(String paramsJson) {
        this.setParamsJson(paramsJson);
        return this;
    }

    public void setParamsJson(String paramsJson) {
        this.paramsJson = paramsJson;
    }

    public String getHeadersJson() {
        return this.headersJson;
    }

    public SearchConfiguration headersJson(String headersJson) {
        this.setHeadersJson(headersJson);
        return this;
    }

    public void setHeadersJson(String headersJson) {
        this.headersJson = headersJson;
    }

    public Instant getDateCreated() {
        return this.dateCreated;
    }

    public SearchConfiguration dateCreated(Instant dateCreated) {
        this.setDateCreated(dateCreated);
        return this;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Instant getLastUpdated() {
        return this.lastUpdated;
    }

    public SearchConfiguration lastUpdated(Instant lastUpdated) {
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
            this.tags.forEach(i -> i.setSearchConfiguration(null));
        }
        if (tags != null) {
            tags.forEach(i -> i.setSearchConfiguration(this));
        }
        this.tags = tags;
    }

    public SearchConfiguration tags(Set<Tag> tags) {
        this.setTags(tags);
        return this;
    }

    public SearchConfiguration addTag(Tag tag) {
        this.tags.add(tag);
        tag.setSearchConfiguration(this);
        return this;
    }

    public SearchConfiguration removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.setSearchConfiguration(null);
        return this;
    }

    public Set<Comment> getComments() {
        return this.comments;
    }

    public void setComments(Set<Comment> comments) {
        if (this.comments != null) {
            this.comments.forEach(i -> i.setSearchConfiguration(null));
        }
        if (comments != null) {
            comments.forEach(i -> i.setSearchConfiguration(this));
        }
        this.comments = comments;
    }

    public SearchConfiguration comments(Set<Comment> comments) {
        this.setComments(comments);
        return this;
    }

    public SearchConfiguration addComment(Comment comment) {
        this.comments.add(comment);
        comment.setSearchConfiguration(this);
        return this;
    }

    public SearchConfiguration removeComment(Comment comment) {
        this.comments.remove(comment);
        comment.setSearchConfiguration(null);
        return this;
    }

    public Set<Analyzer> getAnalyzers() {
        return this.analyzers;
    }

    public void setAnalyzers(Set<Analyzer> analyzers) {
        if (this.analyzers != null) {
            this.analyzers.forEach(i -> i.setSearchConfiguration(null));
        }
        if (analyzers != null) {
            analyzers.forEach(i -> i.setSearchConfiguration(this));
        }
        this.analyzers = analyzers;
    }

    public SearchConfiguration analyzers(Set<Analyzer> analyzers) {
        this.setAnalyzers(analyzers);
        return this;
    }

    public SearchConfiguration addAnalyzer(Analyzer analyzer) {
        this.analyzers.add(analyzer);
        analyzer.setSearchConfiguration(this);
        return this;
    }

    public SearchConfiguration removeAnalyzer(Analyzer analyzer) {
        this.analyzers.remove(analyzer);
        analyzer.setSearchConfiguration(null);
        return this;
    }

    public User getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(User user) {
        this.createdBy = user;
    }

    public SearchConfiguration createdBy(User user) {
        this.setCreatedBy(user);
        return this;
    }

    public Set<Search> getSearches() {
        return this.searches;
    }

    public void setSearches(Set<Search> searches) {
        if (this.searches != null) {
            this.searches.forEach(i -> i.removeConfigurations(this));
        }
        if (searches != null) {
            searches.forEach(i -> i.addConfigurations(this));
        }
        this.searches = searches;
    }

    public SearchConfiguration searches(Set<Search> searches) {
        this.setSearches(searches);
        return this;
    }

    public SearchConfiguration addSearch(Search search) {
        this.searches.add(search);
        search.getConfigurations().add(this);
        return this;
    }

    public SearchConfiguration removeSearch(Search search) {
        this.searches.remove(search);
        search.getConfigurations().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SearchConfiguration)) {
            return false;
        }
        return getId() != null && getId().equals(((SearchConfiguration) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SearchConfiguration{" +
            "id=" + getId() +
            ", label='" + getLabel() + "'" +
            ", description='" + getDescription() + "'" +
            ", defaultConfig='" + getDefaultConfig() + "'" +
            ", url='" + getUrl() + "'" +
            ", paramsJson='" + getParamsJson() + "'" +
            ", headersJson='" + getHeadersJson() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            "}";
    }
}
