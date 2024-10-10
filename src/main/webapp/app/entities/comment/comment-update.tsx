import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntities as getContents } from 'app/entities/content/content.reducer';
import { getEntities as getContentFragments } from 'app/entities/content-fragment/content-fragment.reducer';
import { getEntities as getContexts } from 'app/entities/context/context.reducer';
import { getEntities as getOrganizations } from 'app/entities/organization/organization.reducer';
import { getEntities as getSearches } from 'app/entities/search/search.reducer';
import { getEntities as getSearchConfigurations } from 'app/entities/search-configuration/search-configuration.reducer';
import { getEntities as getSearchResults } from 'app/entities/search-result/search-result.reducer';
import { getEntities as getTags } from 'app/entities/tag/tag.reducer';
import { getEntities as getThingTypes } from 'app/entities/thing-type/thing-type.reducer';
import { getEntities as getTopics } from 'app/entities/topic/topic.reducer';
import { createEntity, getEntity, reset, updateEntity } from './comment.reducer';

export const CommentUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const contents = useAppSelector(state => state.content.entities);
  const contentFragments = useAppSelector(state => state.contentFragment.entities);
  const contexts = useAppSelector(state => state.context.entities);
  const organizations = useAppSelector(state => state.organization.entities);
  const searches = useAppSelector(state => state.search.entities);
  const searchConfigurations = useAppSelector(state => state.searchConfiguration.entities);
  const searchResults = useAppSelector(state => state.searchResult.entities);
  const tags = useAppSelector(state => state.tag.entities);
  const thingTypes = useAppSelector(state => state.thingType.entities);
  const topics = useAppSelector(state => state.topic.entities);
  const commentEntity = useAppSelector(state => state.comment.entity);
  const loading = useAppSelector(state => state.comment.loading);
  const updating = useAppSelector(state => state.comment.updating);
  const updateSuccess = useAppSelector(state => state.comment.updateSuccess);

  const handleClose = () => {
    navigate('/comment');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
    dispatch(getContents({}));
    dispatch(getContentFragments({}));
    dispatch(getContexts({}));
    dispatch(getOrganizations({}));
    dispatch(getSearches({}));
    dispatch(getSearchConfigurations({}));
    dispatch(getSearchResults({}));
    dispatch(getTags({}));
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
    values.lastUpdate = convertDateTimeToServer(values.lastUpdate);

    const entity = {
      ...commentEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user?.toString()),
      content: contents.find(it => it.id.toString() === values.content?.toString()),
      contentFragment: contentFragments.find(it => it.id.toString() === values.contentFragment?.toString()),
      context: contexts.find(it => it.id.toString() === values.context?.toString()),
      organization: organizations.find(it => it.id.toString() === values.organization?.toString()),
      search: searches.find(it => it.id.toString() === values.search?.toString()),
      searchConfiguration: searchConfigurations.find(it => it.id.toString() === values.searchConfiguration?.toString()),
      searchResult: searchResults.find(it => it.id.toString() === values.searchResult?.toString()),
      tag: tags.find(it => it.id.toString() === values.tag?.toString()),
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
          lastUpdate: displayDefaultDateTime(),
        }
      : {
          ...commentEntity,
          dateCreated: convertDateTimeFromServer(commentEntity.dateCreated),
          lastUpdate: convertDateTimeFromServer(commentEntity.lastUpdate),
          user: commentEntity?.user?.id,
          content: commentEntity?.content?.id,
          contentFragment: commentEntity?.contentFragment?.id,
          context: commentEntity?.context?.id,
          organization: commentEntity?.organization?.id,
          search: commentEntity?.search?.id,
          searchConfiguration: commentEntity?.searchConfiguration?.id,
          searchResult: commentEntity?.searchResult?.id,
          tag: commentEntity?.tag?.id,
          thingType: commentEntity?.thingType?.id,
          topic: commentEntity?.topic?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="courageousSearchApp.comment.home.createOrEditLabel" data-cy="CommentCreateUpdateHeading">
            Create or edit a Comment
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="comment-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Label"
                id="comment-label"
                name="label"
                data-cy="label"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField label="Description" id="comment-description" name="description" data-cy="description" type="text" />
              <ValidatedField label="Url" id="comment-url" name="url" data-cy="url" type="text" />
              <ValidatedField
                label="Date Created"
                id="comment-dateCreated"
                name="dateCreated"
                data-cy="dateCreated"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label="Last Update"
                id="comment-lastUpdate"
                name="lastUpdate"
                data-cy="lastUpdate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField id="comment-user" name="user" data-cy="user" label="User" type="select">
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="comment-content" name="content" data-cy="content" label="Content" type="select">
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
                id="comment-contentFragment"
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
              <ValidatedField id="comment-context" name="context" data-cy="context" label="Context" type="select">
                <option value="" key="0" />
                {contexts
                  ? contexts.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="comment-organization" name="organization" data-cy="organization" label="Organization" type="select">
                <option value="" key="0" />
                {organizations
                  ? organizations.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="comment-search" name="search" data-cy="search" label="Search" type="select">
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
                id="comment-searchConfiguration"
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
              <ValidatedField id="comment-searchResult" name="searchResult" data-cy="searchResult" label="Search Result" type="select">
                <option value="" key="0" />
                {searchResults
                  ? searchResults.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="comment-tag" name="tag" data-cy="tag" label="Tag" type="select">
                <option value="" key="0" />
                {tags
                  ? tags.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="comment-thingType" name="thingType" data-cy="thingType" label="Thing Type" type="select">
                <option value="" key="0" />
                {thingTypes
                  ? thingTypes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="comment-topic" name="topic" data-cy="topic" label="Topic" type="select">
                <option value="" key="0" />
                {topics
                  ? topics.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/comment" replace color="info">
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

export default CommentUpdate;
