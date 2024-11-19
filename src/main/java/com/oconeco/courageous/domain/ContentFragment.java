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
 * A ContentFragment.
 */
@Entity
@Table(name = "content_fragment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ContentFragment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "label", nullable = false)
    private String label;

    @Column(name = "text")
    private String text;

    @Column(name = "description")
    private String description;

    @Column(name = "structured_content")
    private String structuredContent;

    @Column(name = "start_pos")
    private Long startPos;

    @Column(name = "end_pos")
    private Long endPos;

    @Column(name = "start_term_num")
    private Long startTermNum;

    @Column(name = "end_term_num")
    private Long endTermNum;

    @Column(name = "subtype")
    private String subtype;

    @Column(name = "language")
    private String language;

    @Column(name = "date_created")
    private Instant dateCreated;

    @Column(name = "last_updated")
    private Instant lastUpdated;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "contentFragment")
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "contentFragment")
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
    @JsonIgnoreProperties(value = { "tags", "comments" }, allowSetters = true)
    private ThingType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "contentFragments", "tags", "comments", "searchResult" }, allowSetters = true)
    private Content content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "analyzers", "tags", "contentFragments", "comments", "createdBy", "organization" }, allowSetters = true)
    private Context context;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "comments", "contentFragments", "tags", "createdBy" }, allowSetters = true)
    private Topic topic;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ContentFragment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return this.label;
    }

    public ContentFragment label(String label) {
        this.setLabel(label);
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getText() {
        return this.text;
    }

    public ContentFragment text(String text) {
        this.setText(text);
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDescription() {
        return this.description;
    }

    public ContentFragment description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStructuredContent() {
        return this.structuredContent;
    }

    public ContentFragment structuredContent(String structuredContent) {
        this.setStructuredContent(structuredContent);
        return this;
    }

    public void setStructuredContent(String structuredContent) {
        this.structuredContent = structuredContent;
    }

    public Long getStartPos() {
        return this.startPos;
    }

    public ContentFragment startPos(Long startPos) {
        this.setStartPos(startPos);
        return this;
    }

    public void setStartPos(Long startPos) {
        this.startPos = startPos;
    }

    public Long getEndPos() {
        return this.endPos;
    }

    public ContentFragment endPos(Long endPos) {
        this.setEndPos(endPos);
        return this;
    }

    public void setEndPos(Long endPos) {
        this.endPos = endPos;
    }

    public Long getStartTermNum() {
        return this.startTermNum;
    }

    public ContentFragment startTermNum(Long startTermNum) {
        this.setStartTermNum(startTermNum);
        return this;
    }

    public void setStartTermNum(Long startTermNum) {
        this.startTermNum = startTermNum;
    }

    public Long getEndTermNum() {
        return this.endTermNum;
    }

    public ContentFragment endTermNum(Long endTermNum) {
        this.setEndTermNum(endTermNum);
        return this;
    }

    public void setEndTermNum(Long endTermNum) {
        this.endTermNum = endTermNum;
    }

    public String getSubtype() {
        return this.subtype;
    }

    public ContentFragment subtype(String subtype) {
        this.setSubtype(subtype);
        return this;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getLanguage() {
        return this.language;
    }

    public ContentFragment language(String language) {
        this.setLanguage(language);
        return this;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Instant getDateCreated() {
        return this.dateCreated;
    }

    public ContentFragment dateCreated(Instant dateCreated) {
        this.setDateCreated(dateCreated);
        return this;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Instant getLastUpdated() {
        return this.lastUpdated;
    }

    public ContentFragment lastUpdated(Instant lastUpdated) {
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
            this.tags.forEach(i -> i.setContentFragment(null));
        }
        if (tags != null) {
            tags.forEach(i -> i.setContentFragment(this));
        }
        this.tags = tags;
    }

    public ContentFragment tags(Set<Tag> tags) {
        this.setTags(tags);
        return this;
    }

    public ContentFragment addTag(Tag tag) {
        this.tags.add(tag);
        tag.setContentFragment(this);
        return this;
    }

    public ContentFragment removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.setContentFragment(null);
        return this;
    }

    public Set<Comment> getComments() {
        return this.comments;
    }

    public void setComments(Set<Comment> comments) {
        if (this.comments != null) {
            this.comments.forEach(i -> i.setContentFragment(null));
        }
        if (comments != null) {
            comments.forEach(i -> i.setContentFragment(this));
        }
        this.comments = comments;
    }

    public ContentFragment comments(Set<Comment> comments) {
        this.setComments(comments);
        return this;
    }

    public ContentFragment addComment(Comment comment) {
        this.comments.add(comment);
        comment.setContentFragment(this);
        return this;
    }

    public ContentFragment removeComment(Comment comment) {
        this.comments.remove(comment);
        comment.setContentFragment(null);
        return this;
    }

    public ThingType getType() {
        return this.type;
    }

    public void setType(ThingType thingType) {
        this.type = thingType;
    }

    public ContentFragment type(ThingType thingType) {
        this.setType(thingType);
        return this;
    }

    public Content getContent() {
        return this.content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public ContentFragment content(Content content) {
        this.setContent(content);
        return this;
    }

    public Context getContext() {
        return this.context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ContentFragment context(Context context) {
        this.setContext(context);
        return this;
    }

    public Topic getTopic() {
        return this.topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public ContentFragment topic(Topic topic) {
        this.setTopic(topic);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContentFragment)) {
            return false;
        }
        return getId() != null && getId().equals(((ContentFragment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContentFragment{" +
            "id=" + getId() +
            ", label='" + getLabel() + "'" +
            ", text='" + getText() + "'" +
            ", description='" + getDescription() + "'" +
            ", structuredContent='" + getStructuredContent() + "'" +
            ", startPos=" + getStartPos() +
            ", endPos=" + getEndPos() +
            ", startTermNum=" + getStartTermNum() +
            ", endTermNum=" + getEndTermNum() +
            ", subtype='" + getSubtype() + "'" +
            ", language='" + getLanguage() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            ", lastUpdated='" + getLastUpdated() + "'" +
            "}";
    }
}
