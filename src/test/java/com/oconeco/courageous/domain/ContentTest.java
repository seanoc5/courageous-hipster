package com.oconeco.courageous.domain;

import static com.oconeco.courageous.domain.CommentTestSamples.*;
import static com.oconeco.courageous.domain.ContentFragmentTestSamples.*;
import static com.oconeco.courageous.domain.ContentTestSamples.*;
import static com.oconeco.courageous.domain.SearchResultTestSamples.*;
import static com.oconeco.courageous.domain.TagTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.oconeco.courageous.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ContentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Content.class);
        Content content1 = getContentSample1();
        Content content2 = new Content();
        assertThat(content1).isNotEqualTo(content2);

        content2.setId(content1.getId());
        assertThat(content1).isEqualTo(content2);

        content2 = getContentSample2();
        assertThat(content1).isNotEqualTo(content2);
    }

    @Test
    void contentFragmentTest() {
        Content content = getContentRandomSampleGenerator();
        ContentFragment contentFragmentBack = getContentFragmentRandomSampleGenerator();

        content.addContentFragment(contentFragmentBack);
        assertThat(content.getContentFragments()).containsOnly(contentFragmentBack);
        assertThat(contentFragmentBack.getContent()).isEqualTo(content);

        content.removeContentFragment(contentFragmentBack);
        assertThat(content.getContentFragments()).doesNotContain(contentFragmentBack);
        assertThat(contentFragmentBack.getContent()).isNull();

        content.contentFragments(new HashSet<>(Set.of(contentFragmentBack)));
        assertThat(content.getContentFragments()).containsOnly(contentFragmentBack);
        assertThat(contentFragmentBack.getContent()).isEqualTo(content);

        content.setContentFragments(new HashSet<>());
        assertThat(content.getContentFragments()).doesNotContain(contentFragmentBack);
        assertThat(contentFragmentBack.getContent()).isNull();
    }

    @Test
    void tagTest() {
        Content content = getContentRandomSampleGenerator();
        Tag tagBack = getTagRandomSampleGenerator();

        content.addTag(tagBack);
        assertThat(content.getTags()).containsOnly(tagBack);
        assertThat(tagBack.getContent()).isEqualTo(content);

        content.removeTag(tagBack);
        assertThat(content.getTags()).doesNotContain(tagBack);
        assertThat(tagBack.getContent()).isNull();

        content.tags(new HashSet<>(Set.of(tagBack)));
        assertThat(content.getTags()).containsOnly(tagBack);
        assertThat(tagBack.getContent()).isEqualTo(content);

        content.setTags(new HashSet<>());
        assertThat(content.getTags()).doesNotContain(tagBack);
        assertThat(tagBack.getContent()).isNull();
    }

    @Test
    void commentTest() {
        Content content = getContentRandomSampleGenerator();
        Comment commentBack = getCommentRandomSampleGenerator();

        content.addComment(commentBack);
        assertThat(content.getComments()).containsOnly(commentBack);
        assertThat(commentBack.getContent()).isEqualTo(content);

        content.removeComment(commentBack);
        assertThat(content.getComments()).doesNotContain(commentBack);
        assertThat(commentBack.getContent()).isNull();

        content.comments(new HashSet<>(Set.of(commentBack)));
        assertThat(content.getComments()).containsOnly(commentBack);
        assertThat(commentBack.getContent()).isEqualTo(content);

        content.setComments(new HashSet<>());
        assertThat(content.getComments()).doesNotContain(commentBack);
        assertThat(commentBack.getContent()).isNull();
    }

    @Test
    void searchResultTest() {
        Content content = getContentRandomSampleGenerator();
        SearchResult searchResultBack = getSearchResultRandomSampleGenerator();

        content.setSearchResult(searchResultBack);
        assertThat(content.getSearchResult()).isEqualTo(searchResultBack);

        content.searchResult(null);
        assertThat(content.getSearchResult()).isNull();
    }
}
