package com.oconeco.courageous.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class AnalyzerAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAnalyzerAllPropertiesEquals(Analyzer expected, Analyzer actual) {
        assertAnalyzerAutoGeneratedPropertiesEquals(expected, actual);
        assertAnalyzerAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAnalyzerAllUpdatablePropertiesEquals(Analyzer expected, Analyzer actual) {
        assertAnalyzerUpdatableFieldsEquals(expected, actual);
        assertAnalyzerUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAnalyzerAutoGeneratedPropertiesEquals(Analyzer expected, Analyzer actual) {
        assertThat(expected)
            .as("Verify Analyzer auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAnalyzerUpdatableFieldsEquals(Analyzer expected, Analyzer actual) {
        assertThat(expected)
            .as("Verify Analyzer relevant properties")
            .satisfies(e -> assertThat(e.getLabel()).as("check label").isEqualTo(actual.getLabel()))
            .satisfies(e -> assertThat(e.getDescription()).as("check description").isEqualTo(actual.getDescription()))
            .satisfies(e -> assertThat(e.getCode()).as("check code").isEqualTo(actual.getCode()))
            .satisfies(e -> assertThat(e.getDateCreated()).as("check dateCreated").isEqualTo(actual.getDateCreated()))
            .satisfies(e -> assertThat(e.getLastUpdated()).as("check lastUpdated").isEqualTo(actual.getLastUpdated()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAnalyzerUpdatableRelationshipsEquals(Analyzer expected, Analyzer actual) {
        assertThat(expected)
            .as("Verify Analyzer relationships")
            .satisfies(e -> assertThat(e.getContext()).as("check context").isEqualTo(actual.getContext()))
            .satisfies(
                e -> assertThat(e.getSearchConfiguration()).as("check searchConfiguration").isEqualTo(actual.getSearchConfiguration())
            )
            .satisfies(e -> assertThat(e.getSearchResult()).as("check searchResult").isEqualTo(actual.getSearchResult()));
    }
}
