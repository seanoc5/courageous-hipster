import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getSearchConfigurations } from 'app/entities/search-configuration/search-configuration.reducer';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntities as getContexts } from 'app/entities/context/context.reducer';
import { getEntities as getThingTypes } from 'app/entities/thing-type/thing-type.reducer';
import { createEntity, getEntity, reset, updateEntity } from './search.reducer';

export const SearchUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const searchConfigurations = useAppSelector(state => state.searchConfiguration.entities);
  const users = useAppSelector(state => state.userManagement.users);
  const contexts = useAppSelector(state => state.context.entities);
  const thingTypes = useAppSelector(state => state.thingType.entities);
  const searchEntity = useAppSelector(state => state.search.entity);
  const loading = useAppSelector(state => state.search.loading);
  const updating = useAppSelector(state => state.search.updating);
  const updateSuccess = useAppSelector(state => state.search.updateSuccess);

  const handleClose = () => {
    navigate('/search');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getSearchConfigurations({}));
    dispatch(getUsers({}));
    dispatch(getContexts({}));
    dispatch(getThingTypes({}));
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
      ...searchEntity,
      ...values,
      configuration: searchConfigurations.find(it => it.id.toString() === values.configuration?.toString()),
      createdBy: users.find(it => it.id.toString() === values.createdBy?.toString()),
      context: contexts.find(it => it.id.toString() === values.context?.toString()),
      type: thingTypes.find(it => it.id.toString() === values.type?.toString()),
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
          ...searchEntity,
          dateCreated: convertDateTimeFromServer(searchEntity.dateCreated),
          lastUpdated: convertDateTimeFromServer(searchEntity.lastUpdated),
          configuration: searchEntity?.configuration?.id,
          createdBy: searchEntity?.createdBy?.id,
          context: searchEntity?.context?.id,
          type: searchEntity?.type?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="courageousSearchApp.search.home.createOrEditLabel" data-cy="SearchCreateUpdateHeading">
            Create or edit a Search
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="search-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Query"
                id="search-query"
                name="query"
                data-cy="query"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Additional Params"
                id="search-additionalParams"
                name="additionalParams"
                data-cy="additionalParams"
                type="textarea"
              />
              <ValidatedField
                label="Date Created"
                id="search-dateCreated"
                name="dateCreated"
                data-cy="dateCreated"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label="Last Updated"
                id="search-lastUpdated"
                name="lastUpdated"
                data-cy="lastUpdated"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField id="search-configuration" name="configuration" data-cy="configuration" label="Configuration" type="select">
                <option value="" key="0" />
                {searchConfigurations
                  ? searchConfigurations.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="search-createdBy" name="createdBy" data-cy="createdBy" label="Created By" type="select">
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="search-context" name="context" data-cy="context" label="Context" type="select">
                <option value="" key="0" />
                {contexts
                  ? contexts.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="search-type" name="type" data-cy="type" label="Type" type="select">
                <option value="" key="0" />
                {thingTypes
                  ? thingTypes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/search" replace color="info">
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

export default SearchUpdate;
