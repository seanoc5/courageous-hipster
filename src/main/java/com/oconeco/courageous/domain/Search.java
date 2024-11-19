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
 * A Search.
 */
@Entity
@Table(name = "search")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Search implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "query", nullable = false)
    private String query;

    @Column(name = "additional_params")
    private String additionalParams;

    @Column(name = "date_created")
    private Instant dateCreated;

    @Column(name = "last_updated")
    private Instant lastUpdated;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "search")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "contents", "tags", "comments", "analyzers", "config", "search" }, allowSetters = true)
    private Set<SearchResult> searchResults = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "rel_search__tags", joinColumns = @JoinColumn(name = "search_id"), inverseJoinColumns = @JoinColumn(name = "tags_id"))
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "search")
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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_search__configurations",
        joinColumns = @JoinColumn(name = "search_id"),
        inverseJoinColumns = @JoinColumn(name = "configurations_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tags", "comments", "analyzers", "createdBy", "searches" }, allowSetters = true)
    private Set<SearchConfiguration> configurations = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "analyzers", "tags", "contentFragments", "comments", "createdBy", "organization" }, allowSetters = true)
    private Context context;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tags", "comments" }, allowSetters = true)
    private ThingType type;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Search id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuery() {
        return this.query;
    }

    public Search query(String query) {
        this.setQuery(query);
        return this;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getAdditionalParams() {
        return this.additionalParams;
    }

    public Search additionalParams(String additionalParams) {
        this.setAdditionalParams(additionalParams);
        return this;
    }

    public void setAdditionalParams(String additionalParams) {
        this.additionalParams = additionalParams;
    }

    public Instant getDateCreated() {
        return this.dateCreated;
    }

    public Search dateCreated(Instant dateCreated) {
        this.setDateCreated(dateCreated);
        return this;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Instant getLastUpdated() {
        return this.lastUpdated;
    }

    public Search lastUpdated(Instant lastUpdated) {
        this.setLastUpdated(lastUpdated);
        return this;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Set<SearchResult> getSearchResults() {
        return this.searchResults;
    }

    public void setSearchResults(Set<SearchResult> searchResults) {
        if (this.searchResults != null) {
            this.searchResults.forEach(i -> i.setSearch(null));
        }
        if (searchResults != null) {
            searchResults.forEach(i -> i.setSearch(this));
        }
        this.searchResults = searchResults;
    }

    public Search searchResults(Set<SearchResult> searchResults) {
        this.setSearchResults(searchResults);
        return this;
    }

    public Search addSearchResult(SearchResult searchResult) {
        this.searchResults.add(searchResult);
        searchResult.setSearch(this);
        return this;
    }

    public Search removeSearchResult(SearchResult searchResult) {
        this.searchResults.remove(searchResult);
        searchResult.setSearch(null);
        return this;
    }

    public Set<Tag> getTags() {
        return this.tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Search tags(Set<Tag> tags) {
        this.setTags(tags);
        return this;
    }

    public Search addTags(Tag tag) {
        this.tags.add(tag);
        return this;
    }

    public Search removeTags(Tag tag) {
        this.tags.remove(tag);
        return this;
    }

    public Set<Comment> getComments() {
        return this.comments;
    }

    public void setComments(Set<Comment> comments) {
        if (this.comments != null) {
            this.comments.forEach(i -> i.setSearch(null));
        }
        if (comments != null) {
            comments.forEach(i -> i.setSearch(this));
        }
        this.comments = comments;
    }

    public Search comments(Set<Comment> comments) {
        this.setComments(comments);
        return this;
    }

    public Search addComment(Comment comment) {
        this.comments.add(comment);
        comment.setSearch(this);
        return this;
    }

    public Search removeComment(Comment comment) {
        this.comments.remove(comment);
        comment.setSearch(null);
        return this;
    }

    public Set<SearchConfiguration> getConfigurations() {
        return this.configurations;
    }

    public void setConfigurations(Set<SearchConfiguration> searchConfigurations) {
        this.configurations = searchConfigurations;
    }

    public Search configurations(Set<SearchConfiguration> searchConfigurations) {
        this.setConfigurations(searchConfigurations);
        return this;
    }

    public Search addConfigurations(SearchConfiguration searchConfiguration) {
        this.configurations.add(searchConfiguration);
        return this;
    }

    public Search removeConfigurations(SearchConfiguration searchConfiguration) {
        this.configurations.remove(searchConfiguration);
        return this;
    }

    public User getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(User user) {
        this.createdBy = user;
    }

    public Search createdBy(User user) {
        this.setCreatedBy(user);
        return this;
    }

    public Context getContext() {
        return this.context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Search context(Context context) {
        this.setContext(context);
        return this;
    }

    public ThingType getType() {
        return this.type;
    }

    public void setType(ThingType thingType) {
        this.type = thingType;
    }

    public Search type(ThingType thingType) {
        this.setType(thingType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Search)) {
            return false;
        }
        return getId() != null && getId().equals(((Search) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Search{" +
            "id=" + getId() +
            ", query='" + getQuery() + "'" +
            ", additionalParams='" + getAdditionalParams() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            "}";
    }
}
