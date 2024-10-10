package com.oconeco.courageous.domain;

import static com.oconeco.courageous.domain.CommentTestSamples.*;
import static com.oconeco.courageous.domain.TagTestSamples.*;
import static com.oconeco.courageous.domain.ThingTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.oconeco.courageous.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ThingTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ThingType.class);
        ThingType thingType1 = getThingTypeSample1();
        ThingType thingType2 = new ThingType();
        assertThat(thingType1).isNotEqualTo(thingType2);

        thingType2.setId(thingType1.getId());
        assertThat(thingType1).isEqualTo(thingType2);

        thingType2 = getThingTypeSample2();
        assertThat(thingType1).isNotEqualTo(thingType2);
    }

    @Test
    void tagTest() {
        ThingType thingType = getThingTypeRandomSampleGenerator();
        Tag tagBack = getTagRandomSampleGenerator();

        thingType.addTag(tagBack);
        assertThat(thingType.getTags()).containsOnly(tagBack);
        assertThat(tagBack.getThingType()).isEqualTo(thingType);

        thingType.removeTag(tagBack);
        assertThat(thingType.getTags()).doesNotContain(tagBack);
        assertThat(tagBack.getThingType()).isNull();

        thingType.tags(new HashSet<>(Set.of(tagBack)));
        assertThat(thingType.getTags()).containsOnly(tagBack);
        assertThat(tagBack.getThingType()).isEqualTo(thingType);

        thingType.setTags(new HashSet<>());
        assertThat(thingType.getTags()).doesNotContain(tagBack);
        assertThat(tagBack.getThingType()).isNull();
    }

    @Test
    void commentTest() {
        ThingType thingType = getThingTypeRandomSampleGenerator();
        Comment commentBack = getCommentRandomSampleGenerator();

        thingType.addComment(commentBack);
        assertThat(thingType.getComments()).containsOnly(commentBack);
        assertThat(commentBack.getThingType()).isEqualTo(thingType);

        thingType.removeComment(commentBack);
        assertThat(thingType.getComments()).doesNotContain(commentBack);
        assertThat(commentBack.getThingType()).isNull();

        thingType.comments(new HashSet<>(Set.of(commentBack)));
        assertThat(thingType.getComments()).containsOnly(commentBack);
        assertThat(commentBack.getThingType()).isEqualTo(thingType);

        thingType.setComments(new HashSet<>());
        assertThat(thingType.getComments()).doesNotContain(commentBack);
        assertThat(commentBack.getThingType()).isNull();
    }
}
