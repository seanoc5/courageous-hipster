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
 * A SearchResult.
 */
@Entity
@Table(name = "search_result")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SearchResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "query", nullable = false)
    private String query;

    @Column(name = "type")
    private String type;

    @Lob
    @Column(name = "response_body")
    private String responseBody;

    @Column(name = "status_code")
    private Integer statusCode;

    @Column(name = "date_created")
    private Instant dateCreated;

    @Column(name = "last_updated")
    private Instant lastUpdated;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "searchResult")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "contentFragments", "tags", "comments", "searchResult" }, allowSetters = true)
    private Set<Content> contents = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "searchResult")
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "searchResult")
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "searchResult")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tags", "createdBy", "context", "searchConfiguration", "searchResult" }, allowSetters = true)
    private Set<Analyzer> analyzers = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tags", "comments", "analyzers", "createdBy" }, allowSetters = true)
    private SearchConfiguration config;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "searchResults", "tags", "comments", "configuration", "createdBy", "context", "type" },
        allowSetters = true
    )
    private Search search;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SearchResult id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuery() {
        return this.query;
    }

    public SearchResult query(String query) {
        this.setQuery(query);
        return this;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getType() {
        return this.type;
    }

    public SearchResult type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getResponseBody() {
        return this.responseBody;
    }

    public SearchResult responseBody(String responseBody) {
        this.setResponseBody(responseBody);
        return this;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public Integer getStatusCode() {
        return this.statusCode;
    }

    public SearchResult statusCode(Integer statusCode) {
        this.setStatusCode(statusCode);
        return this;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Instant getDateCreated() {
        return this.dateCreated;
    }

    public SearchResult dateCreated(Instant dateCreated) {
        this.setDateCreated(dateCreated);
        return this;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Instant getLastUpdated() {
        return this.lastUpdated;
    }

    public SearchResult lastUpdated(Instant lastUpdated) {
        this.setLastUpdated(lastUpdated);
        return this;
    }

    public void setLastUpdated(Instant lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Set<Content> getContents() {
        return this.contents;
    }

    public void setContents(Set<Content> contents) {
        if (this.contents != null) {
            this.contents.forEach(i -> i.setSearchResult(null));
        }
        if (contents != null) {
            contents.forEach(i -> i.setSearchResult(this));
        }
        this.contents = contents;
    }

    public SearchResult contents(Set<Content> contents) {
        this.setContents(contents);
        return this;
    }

    public SearchResult addContent(Content content) {
        this.contents.add(content);
        content.setSearchResult(this);
        return this;
    }

    public SearchResult removeContent(Content content) {
        this.contents.remove(content);
        content.setSearchResult(null);
        return this;
    }

    public Set<Tag> getTags() {
        return this.tags;
    }

    public void setTags(Set<Tag> tags) {
        if (this.tags != null) {
            this.tags.forEach(i -> i.setSearchResult(null));
        }
        if (tags != null) {
            tags.forEach(i -> i.setSearchResult(this));
        }
        this.tags = tags;
    }

    public SearchResult tags(Set<Tag> tags) {
        this.setTags(tags);
        return this;
    }

    public SearchResult addTag(Tag tag) {
        this.tags.add(tag);
        tag.setSearchResult(this);
        return this;
    }

    public SearchResult removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.setSearchResult(null);
        return this;
    }

    public Set<Comment> getComments() {
        return this.comments;
    }

    public void setComments(Set<Comment> comments) {
        if (this.comments != null) {
            this.comments.forEach(i -> i.setSearchResult(null));
        }
        if (comments != null) {
            comments.forEach(i -> i.setSearchResult(this));
        }
        this.comments = comments;
    }

    public SearchResult comments(Set<Comment> comments) {
        this.setComments(comments);
        return this;
    }

    public SearchResult addComment(Comment comment) {
        this.comments.add(comment);
        comment.setSearchResult(this);
        return this;
    }

    public SearchResult removeComment(Comment comment) {
        this.comments.remove(comment);
        comment.setSearchResult(null);
        return this;
    }

    public Set<Analyzer> getAnalyzers() {
        return this.analyzers;
    }

    public void setAnalyzers(Set<Analyzer> analyzers) {
        if (this.analyzers != null) {
            this.analyzers.forEach(i -> i.setSearchResult(null));
        }
        if (analyzers != null) {
            analyzers.forEach(i -> i.setSearchResult(this));
        }
        this.analyzers = analyzers;
    }

    public SearchResult analyzers(Set<Analyzer> analyzers) {
        this.setAnalyzers(analyzers);
        return this;
    }

    public SearchResult addAnalyzer(Analyzer analyzer) {
        this.analyzers.add(analyzer);
        analyzer.setSearchResult(this);
        return this;
    }

    public SearchResult removeAnalyzer(Analyzer analyzer) {
        this.analyzers.remove(analyzer);
        analyzer.setSearchResult(null);
        return this;
    }

    public SearchConfiguration getConfig() {
        return this.config;
    }

    public void setConfig(SearchConfiguration searchConfiguration) {
        this.config = searchConfiguration;
    }

    public SearchResult config(SearchConfiguration searchConfiguration) {
        this.setConfig(searchConfiguration);
        return this;
    }

    public Search getSearch() {
        return this.search;
    }

    public void setSearch(Search search) {
        this.search = search;
    }

    public SearchResult search(Search search) {
        this.setSearch(search);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SearchResult)) {
            return false;
        }
        return getId() != null && getId().equals(((SearchResult) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SearchResult{" +
            "id=" + getId() +
            ", query='" + getQuery() + "'" +
            ", type='" + getType() + "'" +
            ", responseBody='" + getResponseBody() + "'" +
            ", statusCode=" + getStatusCode() +
            ", dateCreated='" + getDateCreated() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            "}";
    }
}
