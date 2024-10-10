import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getSearchConfigurations } from 'app/entities/search-configuration/search-configuration.reducer';
import { getEntities as getSearches } from 'app/entities/search/search.reducer';
import { createEntity, getEntity, reset, updateEntity } from './search-result.reducer';

export const SearchResultUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const searchConfigurations = useAppSelector(state => state.searchConfiguration.entities);
  const searches = useAppSelector(state => state.search.entities);
  const searchResultEntity = useAppSelector(state => state.searchResult.entity);
  const loading = useAppSelector(state => state.searchResult.loading);
  const updating = useAppSelector(state => state.searchResult.updating);
  const updateSuccess = useAppSelector(state => state.searchResult.updateSuccess);

  const handleClose = () => {
    navigate('/search-result');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getSearchConfigurations({}));
    dispatch(getSearches({}));
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
    if (values.statusCode !== undefined && typeof values.statusCode !== 'number') {
      values.statusCode = Number(values.statusCode);
    }
    values.dateCreated = convertDateTimeToServer(values.dateCreated);
    values.lastUpdated = convertDateTimeToServer(values.lastUpdated);

    const entity = {
      ...searchResultEntity,
      ...values,
      config: searchConfigurations.find(it => it.id.toString() === values.config?.toString()),
      search: searches.find(it => it.id.toString() === values.search?.toString()),
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
          ...searchResultEntity,
          dateCreated: convertDateTimeFromServer(searchResultEntity.dateCreated),
          lastUpdated: convertDateTimeFromServer(searchResultEntity.lastUpdated),
          config: searchResultEntity?.config?.id,
          search: searchResultEntity?.search?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="courageousSearchApp.searchResult.home.createOrEditLabel" data-cy="SearchResultCreateUpdateHeading">
            Create or edit a Search Result
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField name="id" required readOnly id="search-result-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Query"
                id="search-result-query"
                name="query"
                data-cy="query"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField label="Type" id="search-result-type" name="type" data-cy="type" type="text" />
              <ValidatedField
                label="Response Body"
                id="search-result-responseBody"
                name="responseBody"
                data-cy="responseBody"
                type="textarea"
              />
              <ValidatedField label="Status Code" id="search-result-statusCode" name="statusCode" data-cy="statusCode" type="text" />
              <ValidatedField
                label="Date Created"
                id="search-result-dateCreated"
                name="dateCreated"
                data-cy="dateCreated"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label="Last Updated"
                id="search-result-lastUpdated"
                name="lastUpdated"
                data-cy="lastUpdated"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField id="search-result-config" name="config" data-cy="config" label="Config" type="select">
                <option value="" key="0" />
                {searchConfigurations
                  ? searchConfigurations.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="search-result-search" name="search" data-cy="search" label="Search" type="select">
                <option value="" key="0" />
                {searches
                  ? searches.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/search-result" replace color="info">
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

export default SearchResultUpdate;
