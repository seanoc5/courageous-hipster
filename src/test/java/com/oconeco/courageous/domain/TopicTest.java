package com.oconeco.courageous.domain;

import static com.oconeco.courageous.domain.CommentTestSamples.*;
import static com.oconeco.courageous.domain.ContentFragmentTestSamples.*;
import static com.oconeco.courageous.domain.TagTestSamples.*;
import static com.oconeco.courageous.domain.TopicTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.oconeco.courageous.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TopicTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Topic.class);
        Topic topic1 = getTopicSample1();
        Topic topic2 = new Topic();
        assertThat(topic1).isNotEqualTo(topic2);

        topic2.setId(topic1.getId());
        assertThat(topic1).isEqualTo(topic2);

        topic2 = getTopicSample2();
        assertThat(topic1).isNotEqualTo(topic2);
    }

    @Test
    void commentTest() {
        Topic topic = getTopicRandomSampleGenerator();
        Comment commentBack = getCommentRandomSampleGenerator();

        topic.addComment(commentBack);
        assertThat(topic.getComments()).containsOnly(commentBack);
        assertThat(commentBack.getTopic()).isEqualTo(topic);

        topic.removeComment(commentBack);
        assertThat(topic.getComments()).doesNotContain(commentBack);
        assertThat(commentBack.getTopic()).isNull();

        topic.comments(new HashSet<>(Set.of(commentBack)));
        assertThat(topic.getComments()).containsOnly(commentBack);
        assertThat(commentBack.getTopic()).isEqualTo(topic);

        topic.setComments(new HashSet<>());
        assertThat(topic.getComments()).doesNotContain(commentBack);
        assertThat(commentBack.getTopic()).isNull();
    }

    @Test
    void contentFragmentTest() {
        Topic topic = getTopicRandomSampleGenerator();
        ContentFragment contentFragmentBack = getContentFragmentRandomSampleGenerator();

        topic.addContentFragment(contentFragmentBack);
        assertThat(topic.getContentFragments()).containsOnly(contentFragmentBack);
        assertThat(contentFragmentBack.getTopic()).isEqualTo(topic);

        topic.removeContentFragment(contentFragmentBack);
        assertThat(topic.getContentFragments()).doesNotContain(contentFragmentBack);
        assertThat(contentFragmentBack.getTopic()).isNull();

        topic.contentFragments(new HashSet<>(Set.of(contentFragmentBack)));
        assertThat(topic.getContentFragments()).containsOnly(contentFragmentBack);
        assertThat(contentFragmentBack.getTopic()).isEqualTo(topic);

        topic.setContentFragments(new HashSet<>());
        assertThat(topic.getContentFragments()).doesNotContain(contentFragmentBack);
        assertThat(contentFragmentBack.getTopic()).isNull();
    }

    @Test
    void tagTest() {
        Topic topic = getTopicRandomSampleGenerator();
        Tag tagBack = getTagRandomSampleGenerator();

        topic.addTag(tagBack);
        assertThat(topic.getTags()).containsOnly(tagBack);
        assertThat(tagBack.getTopic()).isEqualTo(topic);

        topic.removeTag(tagBack);
        assertThat(topic.getTags()).doesNotContain(tagBack);
        assertThat(tagBack.getTopic()).isNull();

        topic.tags(new HashSet<>(Set.of(tagBack)));
        assertThat(topic.getTags()).containsOnly(tagBack);
        assertThat(tagBack.getTopic()).isEqualTo(topic);

        topic.setTags(new HashSet<>());
        assertThat(topic.getTags()).doesNotContain(tagBack);
        assertThat(tagBack.getTopic()).isNull();
    }
}
