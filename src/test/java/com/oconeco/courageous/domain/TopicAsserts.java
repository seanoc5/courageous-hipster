package com.oconeco.courageous.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class TopicAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTopicAllPropertiesEquals(Topic expected, Topic actual) {
        assertTopicAutoGeneratedPropertiesEquals(expected, actual);
        assertTopicAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTopicAllUpdatablePropertiesEquals(Topic expected, Topic actual) {
        assertTopicUpdatableFieldsEquals(expected, actual);
        assertTopicUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTopicAutoGeneratedPropertiesEquals(Topic expected, Topic actual) {
        assertThat(expected)
            .as("Verify Topic auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTopicUpdatableFieldsEquals(Topic expected, Topic actual) {
        assertThat(expected)
            .as("Verify Topic relevant properties")
            .satisfies(e -> assertThat(e.getLabel()).as("check label").isEqualTo(actual.getLabel()))
            .satisfies(e -> assertThat(e.getDescription()).as("check description").isEqualTo(actual.getDescription()))
            .satisfies(e -> assertThat(e.getDefaultTopic()).as("check defaultTopic").isEqualTo(actual.getDefaultTopic()))
            .satisfies(e -> assertThat(e.getLevel()).as("check level").isEqualTo(actual.getLevel()))
            .satisfies(e -> assertThat(e.getDateCreated()).as("check dateCreated").isEqualTo(actual.getDateCreated()))
            .satisfies(e -> assertThat(e.getLastUpdated()).as("check lastUpdated").isEqualTo(actual.getLastUpdated()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTopicUpdatableRelationshipsEquals(Topic expected, Topic actual) {
        // empty method
    }
}
