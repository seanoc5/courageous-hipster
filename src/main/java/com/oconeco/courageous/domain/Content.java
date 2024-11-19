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
 * A Content.
 */
@Entity
@Table(name = "content")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Content implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "uri", nullable = false)
    private String uri;

    /**
     * snippets back from search engine, or summary from ML, or actual 'description' from author/publisher
     */
    @Schema(description = "snippets back from search engine, or summary from ML, or actual 'description' from author/publisher")
    @Column(name = "description")
    private String description;

    /**
     * url or file path
     */
    @Schema(description = "url or file path")
    @Column(name = "path")
    private String path;

    /**
     * site or machine name
     */
    @Schema(description = "site or machine name")
    @Column(name = "source")
    private String source;

    /**
     * any additional parameters, such as url query params, where relevant (optional)
     */
    @Schema(description = "any additional parameters, such as url query params, where relevant (optional)")
    @Column(name = "params")
    private String params;

    /**
     * extracted text from source document (typically fetched by ContentService.fetchContent(contentDoc)
     */
    @Schema(description = "extracted text from source document (typically fetched by ContentService.fetchContent(contentDoc)")
    @Column(name = "body_text")
    private String bodyText;

    @Column(name = "text_size")
    private Long textSize;

    /**
     * html/xml version typically, but perhaps other (markdown, json,...)
     */
    @Schema(description = "html/xml version typically, but perhaps other (markdown, json,...)")
    @Column(name = "structured_content")
    private String structuredContent;

    @Column(name = "structure_size")
    private Long structureSize;

    @Column(name = "author")
    private String author;

    @Column(name = "language")
    private String language;

    @Column(name = "type")
    private String type;

    @Column(name = "subtype")
    private String subtype;

    @Column(name = "mine_type")
    private String mineType;

    @Column(name = "publish_date")
    private Instant publishDate;

    /**
     * originally added to capture Brave api 'family_friendly' flag info...
     */
    @Schema(description = "originally added to capture Brave api 'family_friendly' flag info...")
    @Column(name = "offensive_flag")
    private String offensiveFlag;

    @Column(name = "favicon")
    private String favicon;

    @Column(name = "date_created")
    private Instant dateCreated;

    @Column(name = "last_update")
    private Instant lastUpdate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "content")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tags", "comments", "type", "content", "context", "topic" }, allowSetters = true)
    private Set<ContentFragment> contentFragments = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "content")
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "content")
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
    @JsonIgnoreProperties(value = { "contents", "tags", "comments", "analyzers", "config", "search" }, allowSetters = true)
    private SearchResult searchResult;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Content id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Content title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUri() {
        return this.uri;
    }

    public Content uri(String uri) {
        this.setUri(uri);
        return this;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getDescription() {
        return this.description;
    }

    public Content description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPath() {
        return this.path;
    }

    public Content path(String path) {
        this.setPath(path);
        return this;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSource() {
        return this.source;
    }

    public Content source(String source) {
        this.setSource(source);
        return this;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getParams() {
        return this.params;
    }

    public Content params(String params) {
        this.setParams(params);
        return this;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getBodyText() {
        return this.bodyText;
    }

    public Content bodyText(String bodyText) {
        this.setBodyText(bodyText);
        return this;
    }

    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
    }

    public Long getTextSize() {
        return this.textSize;
    }

    public Content textSize(Long textSize) {
        this.setTextSize(textSize);
        return this;
    }

    public void setTextSize(Long textSize) {
        this.textSize = textSize;
    }

    public String getStructuredContent() {
        return this.structuredContent;
    }

    public Content structuredContent(String structuredContent) {
        this.setStructuredContent(structuredContent);
        return this;
    }

    public void setStructuredContent(String structuredContent) {
        this.structuredContent = structuredContent;
    }

    public Long getStructureSize() {
        return this.structureSize;
    }

    public Content structureSize(Long structureSize) {
        this.setStructureSize(structureSize);
        return this;
    }

    public void setStructureSize(Long structureSize) {
        this.structureSize = structureSize;
    }

    public String getAuthor() {
        return this.author;
    }

    public Content author(String author) {
        this.setAuthor(author);
        return this;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLanguage() {
        return this.language;
    }

    public Content language(String language) {
        this.setLanguage(language);
        return this;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getType() {
        return this.type;
    }

    public Content type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubtype() {
        return this.subtype;
    }

    public Content subtype(String subtype) {
        this.setSubtype(subtype);
        return this;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getMineType() {
        return this.mineType;
    }

    public Content mineType(String mineType) {
        this.setMineType(mineType);
        return this;
    }

    public void setMineType(String mineType) {
        this.mineType = mineType;
    }

    public Instant getPublishDate() {
        return this.publishDate;
    }

    public Content publishDate(Instant publishDate) {
        this.setPublishDate(publishDate);
        return this;
    }

    public void setPublishDate(Instant publishDate) {
        this.publishDate = publishDate;
    }

    public String getOffensiveFlag() {
        return this.offensiveFlag;
    }

    public Content offensiveFlag(String offensiveFlag) {
        this.setOffensiveFlag(offensiveFlag);
        return this;
    }

    public void setOffensiveFlag(String offensiveFlag) {
        this.offensiveFlag = offensiveFlag;
    }

    public String getFavicon() {
        return this.favicon;
    }

    public Content favicon(String favicon) {
        this.setFavicon(favicon);
        return this;
    }

    public void setFavicon(String favicon) {
        this.favicon = favicon;
    }

    public Instant getDateCreated() {
        return this.dateCreated;
    }

    public Content dateCreated(Instant dateCreated) {
        this.setDateCreated(dateCreated);
        return this;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Instant getLastUpdate() {
        return this.lastUpdate;
    }

    public Content lastUpdate(Instant lastUpdate) {
        this.setLastUpdate(lastUpdate);
        return this;
    }

    public void setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Set<ContentFragment> getContentFragments() {
        return this.contentFragments;
    }

    public void setContentFragments(Set<ContentFragment> contentFragments) {
        if (this.contentFragments != null) {
            this.contentFragments.forEach(i -> i.setContent(null));
        }
        if (contentFragments != null) {
            contentFragments.forEach(i -> i.setContent(this));
        }
        this.contentFragments = contentFragments;
    }

    public Content contentFragments(Set<ContentFragment> contentFragments) {
        this.setContentFragments(contentFragments);
        return this;
    }

    public Content addContentFragment(ContentFragment contentFragment) {
        this.contentFragments.add(contentFragment);
        contentFragment.setContent(this);
        return this;
    }

    public Content removeContentFragment(ContentFragment contentFragment) {
        this.contentFragments.remove(contentFragment);
        contentFragment.setContent(null);
        return this;
    }

    public Set<Tag> getTags() {
        return this.tags;
    }

    public void setTags(Set<Tag> tags) {
        if (this.tags != null) {
            this.tags.forEach(i -> i.setContent(null));
        }
        if (tags != null) {
            tags.forEach(i -> i.setContent(this));
        }
        this.tags = tags;
    }

    public Content tags(Set<Tag> tags) {
        this.setTags(tags);
        return this;
    }

    public Content addTag(Tag tag) {
        this.tags.add(tag);
        tag.setContent(this);
        return this;
    }

    public Content removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.setContent(null);
        return this;
    }

    public Set<Comment> getComments() {
        return this.comments;
    }

    public void setComments(Set<Comment> comments) {
        if (this.comments != null) {
            this.comments.forEach(i -> i.setContent(null));
        }
        if (comments != null) {
            comments.forEach(i -> i.setContent(this));
        }
        this.comments = comments;
    }

    public Content comments(Set<Comment> comments) {
        this.setComments(comments);
        return this;
    }

    public Content addComment(Comment comment) {
        this.comments.add(comment);
        comment.setContent(this);
        return this;
    }

    public Content removeComment(Comment comment) {
        this.comments.remove(comment);
        comment.setContent(null);
        return this;
    }

    public SearchResult getSearchResult() {
        return this.searchResult;
    }

    public void setSearchResult(SearchResult searchResult) {
        this.searchResult = searchResult;
    }

    public Content searchResult(SearchResult searchResult) {
        this.setSearchResult(searchResult);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Content)) {
            return false;
        }
        return getId() != null && getId().equals(((Content) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Content{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", uri='" + getUri() + "'" +
            ", description='" + getDescription() + "'" +
            ", path='" + getPath() + "'" +
            ", source='" + getSource() + "'" +
            ", params='" + getParams() + "'" +
            ", bodyText='" + getBodyText() + "'" +
            ", textSize=" + getTextSize() +
            ", structuredContent='" + getStructuredContent() + "'" +
            ", structureSize=" + getStructureSize() +
            ", author='" + getAuthor() + "'" +
            ", language='" + getLanguage() + "'" +
            ", type='" + getType() + "'" +
            ", subtype='" + getSubtype() + "'" +
            ", mineType='" + getMineType() + "'" +
            ", publishDate='" + getPublishDate() + "'" +
            ", offensiveFlag='" + getOffensiveFlag() + "'" +
            ", favicon='" + getFavicon() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", lastUpdate='" + getLastUpdate() + "'" +
            "}";
    }
}
