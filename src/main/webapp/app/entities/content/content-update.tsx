import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row, UncontrolledTooltip } from 'reactstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getSearchResults } from 'app/entities/search-result/search-result.reducer';
import { createEntity, getEntity, reset, updateEntity } from './content.reducer';

export const ContentUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const searchResults = useAppSelector(state => state.searchResult.entities);
  const contentEntity = useAppSelector(state => state.content.entity);
  const loading = useAppSelector(state => state.content.loading);
  const updating = useAppSelector(state => state.content.updating);
  const updateSuccess = useAppSelector(state => state.content.updateSuccess);

  const handleClose = () => {
    navigate('/content');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getSearchResults({}));
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
    if (values.textSize !== undefined && typeof values.textSize !== 'number') {
      values.textSize = Number(values.textSize);
    }
    if (values.structureSize !== undefined && typeof values.structureSize !== 'number') {
      values.structureSize = Number(values.structureSize);
    }
    values.publishDate = convertDateTimeToServer(values.publishDate);
    values.dateCreated = convertDateTimeToServer(values.dateCreated);
    values.lastUpdate = convertDateTimeToServer(values.lastUpdate);

    const entity = {
      ...contentEntity,
      ...values,
      searchResult: searchResults.find(it => it.id.toString() === values.searchResult?.toString()),
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
          publishDate: displayDefaultDateTime(),
          dateCreated: displayDefaultDateTime(),
          lastUpdate: displayDefaultDateTime(),
        }
      : {
          ...contentEntity,
          publishDate: convertDateTimeFromServer(contentEntity.publishDate),
          dateCreated: convertDateTimeFromServer(contentEntity.dateCreated),
          lastUpdate: convertDateTimeFromServer(contentEntity.lastUpdate),
          searchResult: contentEntity?.searchResult?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="courageousSearchApp.content.home.createOrEditLabel" data-cy="ContentCreateUpdateHeading">
            Create or edit a Content
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="content-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Title"
                id="content-title"
                name="title"
                data-cy="title"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Uri"
                id="content-uri"
                name="uri"
                data-cy="uri"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField label="Description" id="content-description" name="description" data-cy="description" type="textarea" />
              <UncontrolledTooltip target="descriptionLabel">
                snippets back from search engine, or summary from ML, or actual &#39;description&#39; from author/publisher
              </UncontrolledTooltip>
              <ValidatedField label="Path" id="content-path" name="path" data-cy="path" type="text" />
              <UncontrolledTooltip target="pathLabel">url or file path</UncontrolledTooltip>
              <ValidatedField label="Source" id="content-source" name="source" data-cy="source" type="text" />
              <UncontrolledTooltip target="sourceLabel">site or machine name</UncontrolledTooltip>
              <ValidatedField label="Params" id="content-params" name="params" data-cy="params" type="text" />
              <UncontrolledTooltip target="paramsLabel">
                any additional parameters, such as url query params, where relevant (optional)
              </UncontrolledTooltip>
              <ValidatedField label="Body Text" id="content-bodyText" name="bodyText" data-cy="bodyText" type="textarea" />
              <UncontrolledTooltip target="bodyTextLabel">
                extracted text from source document (typically fetched by ContentService.fetchContent(contentDoc)
              </UncontrolledTooltip>
              <ValidatedField label="Text Size" id="content-textSize" name="textSize" data-cy="textSize" type="text" />
              <ValidatedField
                label="Structured Content"
                id="content-structuredContent"
                name="structuredContent"
                data-cy="structuredContent"
                type="textarea"
              />
              <UncontrolledTooltip target="structuredContentLabel">
                html/xml version typically, but perhaps other (markdown, json,...)
              </UncontrolledTooltip>
              <ValidatedField label="Structure Size" id="content-structureSize" name="structureSize" data-cy="structureSize" type="text" />
              <ValidatedField label="Author" id="content-author" name="author" data-cy="author" type="text" />
              <ValidatedField label="Language" id="content-language" name="language" data-cy="language" type="text" />
              <ValidatedField label="Type" id="content-type" name="type" data-cy="type" type="text" />
              <ValidatedField label="Subtype" id="content-subtype" name="subtype" data-cy="subtype" type="text" />
              <ValidatedField label="Mine Type" id="content-mineType" name="mineType" data-cy="mineType" type="text" />
              <ValidatedField
                label="Publish Date"
                id="content-publishDate"
                name="publishDate"
                data-cy="publishDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField label="Offensive Flag" id="content-offensiveFlag" name="offensiveFlag" data-cy="offensiveFlag" type="text" />
              <UncontrolledTooltip target="offensiveFlagLabel">
                originally added to capture Brave api &#39;family_friendly&#39; flag info...
              </UncontrolledTooltip>
              <ValidatedField label="Favicon" id="content-favicon" name="favicon" data-cy="favicon" type="text" />
              <ValidatedField
                label="Date Created"
                id="content-dateCreated"
                name="dateCreated"
                data-cy="dateCreated"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label="Last Update"
                id="content-lastUpdate"
                name="lastUpdate"
                data-cy="lastUpdate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField id="content-searchResult" name="searchResult" data-cy="searchResult" label="Search Result" type="select">
                <option value="" key="0" />
                {searchResults
                  ? searchResults.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/content" replace color="info">
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

export default ContentUpdate;
