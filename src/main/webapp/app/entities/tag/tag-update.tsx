import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntities as getAnalyzers } from 'app/entities/analyzer/analyzer.reducer';
import { getEntities as getContents } from 'app/entities/content/content.reducer';
import { getEntities as getContentFragments } from 'app/entities/content-fragment/content-fragment.reducer';
import { getEntities as getContexts } from 'app/entities/context/context.reducer';
import { getEntities as getOrganizations } from 'app/entities/organization/organization.reducer';
import { getEntities as getSearches } from 'app/entities/search/search.reducer';
import { getEntities as getSearchConfigurations } from 'app/entities/search-configuration/search-configuration.reducer';
import { getEntities as getSearchResults } from 'app/entities/search-result/search-result.reducer';
import { getEntities as getThingTypes } from 'app/entities/thing-type/thing-type.reducer';
import { getEntities as getTopics } from 'app/entities/topic/topic.reducer';
import { createEntity, getEntity, reset, updateEntity } from './tag.reducer';

export const TagUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const analyzers = useAppSelector(state => state.analyzer.entities);
  const contents = useAppSelector(state => state.content.entities);
  const contentFragments = useAppSelector(state => state.contentFragment.entities);
  const contexts = useAppSelector(state => state.context.entities);
  const organizations = useAppSelector(state => state.organization.entities);
  const searches = useAppSelector(state => state.search.entities);
  const searchConfigurations = useAppSelector(state => state.searchConfiguration.entities);
  const searchResults = useAppSelector(state => state.searchResult.entities);
  const thingTypes = useAppSelector(state => state.thingType.entities);
  const topics = useAppSelector(state => state.topic.entities);
  const tagEntity = useAppSelector(state => state.tag.entity);
  const loading = useAppSelector(state => state.tag.loading);
  const updating = useAppSelector(state => state.tag.updating);
  const updateSuccess = useAppSelector(state => state.tag.updateSuccess);

  const handleClose = () => {
    navigate('/tag');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
    dispatch(getAnalyzers({}));
    dispatch(getContents({}));
    dispatch(getContentFragments({}));
    dispatch(getContexts({}));
    dispatch(getOrganizations({}));
    dispatch(getSearches({}));
    dispatch(getSearchConfigurations({}));
    dispatch(getSearchResults({}));
    dispatch(getThingTypes({}));
    dispatch(getTopics({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.dateCreated = convertDateTimeToServer(values.dateCreated);
    values.lastUpdated = convertDateTimeToServer(values.lastUpdated);

    const entity = {
      ...tagEntity,
      ...values,
      createdBy: users.find(it => it.id.toString() === values.createdBy?.toString()),
      analyzer: analyzers.find(it => it.id.toString() === values.analyzer?.toString()),
      content: contents.find(it => it.id.toString() === values.content?.toString()),
      contentFragment: contentFragments.find(it => it.id.toString() === values.contentFragment?.toString()),
      context: contexts.find(it => it.id.toString() === values.context?.toString()),
      organization: organizations.find(it => it.id.toString() === values.organization?.toString()),
      search: searches.find(it => it.id.toString() === values.search?.toString()),
      searchConfiguration: searchConfigurations.find(it => it.id.toString() === values.searchConfiguration?.toString()),
      searchResult: searchResults.find(it => it.id.toString() === values.searchResult?.toString()),
      thingType: thingTypes.find(it => it.id.toString() === values.thingType?.toString()),
      topic: topics.find(it => it.id.toString() === values.topic?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          dateCreated: displayDefaultDateTime(),
          lastUpdated: displayDefaultDateTime(),
        }
      : {
          ...tagEntity,
          dateCreated: convertDateTimeFromServer(tagEntity.dateCreated),
          lastUpdated: convertDateTimeFromServer(tagEntity.lastUpdated),
          createdBy: tagEntity?.createdBy?.id,
          analyzer: tagEntity?.analyzer?.id,
          content: tagEntity?.content?.id,
          contentFragment: tagEntity?.contentFragment?.id,
          context: tagEntity?.context?.id,
          organization: tagEntity?.organization?.id,
          search: tagEntity?.search?.id,
          searchConfiguration: tagEntity?.searchConfiguration?.id,
          searchResult: tagEntity?.searchResult?.id,
          thingType: tagEntity?.thingType?.id,
          topic: tagEntity?.topic?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="courageousSearchApp.tag.home.createOrEditLabel" data-cy="TagCreateUpdateHeading">
            Create or edit a Tag
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="tag-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Label"
                id="tag-label"
                name="label"
                data-cy="label"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField label="Description" id="tag-description" name="description" data-cy="description" type="text" />
              <ValidatedField label="Default Tag" id="tag-defaultTag" name="defaultTag" data-cy="defaultTag" check type="checkbox" />
              <ValidatedField
                label="Date Created"
                id="tag-dateCreated"
                name="dateCreated"
                data-cy="dateCreated"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label="Last Updated"
                id="tag-lastUpdated"
                name="lastUpdated"
                data-cy="lastUpdated"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField id="tag-createdBy" name="createdBy" data-cy="createdBy" label="Created By" type="select">
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="tag-analyzer" name="analyzer" data-cy="analyzer" label="Analyzer" type="select">
                <option value="" key="0" />
                {analyzers
                  ? analyzers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="tag-content" name="content" data-cy="content" label="Content" type="select">
                <option value="" key="0" />
                {contents
                  ? contents.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="tag-contentFragment"
                name="contentFragment"
                data-cy="contentFragment"
                label="Content Fragment"
                type="select"
              >
                <option value="" key="0" />
                {contentFragments
                  ? contentFragments.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="tag-context" name="context" data-cy="context" label="Context" type="select">
                <option value="" key="0" />
                {contexts
                  ? contexts.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="tag-organization" name="organization" data-cy="organization" label="Organization" type="select">
                <option value="" key="0" />
                {organizations
                  ? organizations.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="tag-search" name="search" data-cy="search" label="Search" type="select">
                <option value="" key="0" />
                {searches
                  ? searches.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="tag-searchConfiguration"
                name="searchConfiguration"
                data-cy="searchConfiguration"
                label="Search Configuration"
                type="select"
              >
                <option value="" key="0" />
                {searchConfigurations
                  ? searchConfigurations.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="tag-searchResult" name="searchResult" data-cy="searchResult" label="Search Result" type="select">
                <option value="" key="0" />
                {searchResults
                  ? searchResults.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="tag-thingType" name="thingType" data-cy="thingType" label="Thing Type" type="select">
                <option value="" key="0" />
                {thingTypes
                  ? thingTypes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="tag-topic" name="topic" data-cy="topic" label="Topic" type="select">
                <option value="" key="0" />
                {topics
                  ? topics.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/tag" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default TagUpdate;
