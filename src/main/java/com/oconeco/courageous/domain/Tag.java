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
 * A Tag.
 */
@Entity
@Table(name = "tag")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Tag implements Serializable {

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

    @Column(name = "default_tag")
    private Boolean defaultTag;

    @Column(name = "date_created")
    private Instant dateCreated;

    @Column(name = "last_updated")
    private Instant lastUpdated;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tag")
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
    @JsonIgnoreProperties(value = { "tags", "createdBy", "context", "searchConfiguration", "searchResult" }, allowSetters = true)
    private Analyzer analyzer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "contentFragments", "tags", "comments", "searchResult" }, allowSetters = true)
    private Content content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tags", "comments", "type", "content", "context", "topic" }, allowSetters = true)
    private ContentFragment contentFragment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "analyzers", "tags", "contentFragments", "comments", "createdBy", "organization" }, allowSetters = true)
    private Context context;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "contexts", "tags", "comments" }, allowSetters = true)
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "searchResults", "tags", "comments", "configuration", "createdBy", "context", "type" },
        allowSetters = true
    )
    private Search search;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tags", "comments", "analyzers", "createdBy" }, allowSetters = true)
    private SearchConfiguration searchConfiguration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "contents", "tags", "comments", "analyzers", "config", "search" }, allowSetters = true)
    private SearchResult searchResult;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tags", "comments" }, allowSetters = true)
    private ThingType thingType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "comments", "contentFragments", "tags", "createdBy" }, allowSetters = true)
    private Topic topic;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Tag id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return this.label;
    }

    public Tag label(String label) {
        this.setLabel(label);
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return this.description;
    }

    public Tag description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getDefaultTag() {
        return this.defaultTag;
    }

    public Tag defaultTag(Boolean defaultTag) {
        this.setDefaultTag(defaultTag);
        return this;
    }

    public void setDefaultTag(Boolean defaultTag) {
        this.defaultTag = defaultTag;
    }

    public Instant getDateCreated() {
        return this.dateCreated;
    }

    public Tag dateCreated(Instant dateCreated) {
        this.setDateCreated(dateCreated);
        return this;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Instant getLastUpdated() {
        return this.lastUpdated;
    }

    public Tag lastUpdated(Instant lastUpdated) {
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
            this.comments.forEach(i -> i.setTag(null));
        }
        if (comments != null) {
            comments.forEach(i -> i.setTag(this));
        }
        this.comments = comments;
    }

    public Tag comments(Set<Comment> comments) {
        this.setComments(comments);
        return this;
    }

    public Tag addComment(Comment comment) {
        this.comments.add(comment);
        comment.setTag(this);
        return this;
    }

    public Tag removeComment(Comment comment) {
        this.comments.remove(comment);
        comment.setTag(null);
        return this;
    }

    public User getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(User user) {
        this.createdBy = user;
    }

    public Tag createdBy(User user) {
        this.setCreatedBy(user);
        return this;
    }

    public Analyzer getAnalyzer() {
        return this.analyzer;
    }

    public void setAnalyzer(Analyzer analyzer) {
        this.analyzer = analyzer;
    }

    public Tag analyzer(Analyzer analyzer) {
        this.setAnalyzer(analyzer);
        return this;
    }

    public Content getContent() {
        return this.content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public Tag content(Content content) {
        this.setContent(content);
        return this;
    }

    public ContentFragment getContentFragment() {
        return this.contentFragment;
    }

    public void setContentFragment(ContentFragment contentFragment) {
        this.contentFragment = contentFragment;
    }

    public Tag contentFragment(ContentFragment contentFragment) {
        this.setContentFragment(contentFragment);
        return this;
    }

    public Context getContext() {
        return this.context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Tag context(Context context) {
        this.setContext(context);
        return this;
    }

    public Organization getOrganization() {
        return this.organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Tag organization(Organization organization) {
        this.setOrganization(organization);
        return this;
    }

    public Search getSearch() {
        return this.search;
    }

    public void setSearch(Search search) {
        this.search = search;
    }

    public Tag search(Search search) {
        this.setSearch(search);
        return this;
    }

    public SearchConfiguration getSearchConfiguration() {
        return this.searchConfiguration;
    }

    public void setSearchConfiguration(SearchConfiguration searchConfiguration) {
        this.searchConfiguration = searchConfiguration;
    }

    public Tag searchConfiguration(SearchConfiguration searchConfiguration) {
        this.setSearchConfiguration(searchConfiguration);
        return this;
    }

    public SearchResult getSearchResult() {
        return this.searchResult;
    }

    public void setSearchResult(SearchResult searchResult) {
        this.searchResult = searchResult;
    }

    public Tag searchResult(SearchResult searchResult) {
        this.setSearchResult(searchResult);
        return this;
    }

    public ThingType getThingType() {
        return this.thingType;
    }

    public void setThingType(ThingType thingType) {
        this.thingType = thingType;
    }

    public Tag thingType(ThingType thingType) {
        this.setThingType(thingType);
        return this;
    }

    public Topic getTopic() {
        return this.topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public Tag topic(Topic topic) {
        this.setTopic(topic);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tag)) {
            return false;
        }
        return getId() != null && getId().equals(((Tag) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tag{" +
            "id=" + getId() +
            ", label='" + getLabel() + "'" +
            ", description='" + getDescription() + "'" +
            ", defaultTag='" + getDefaultTag() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            "}";
    }
}
