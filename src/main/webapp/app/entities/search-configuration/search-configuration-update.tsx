import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { createEntity, getEntity, reset, updateEntity } from './search-configuration.reducer';

export const SearchConfigurationUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const searchConfigurationEntity = useAppSelector(state => state.searchConfiguration.entity);
  const loading = useAppSelector(state => state.searchConfiguration.loading);
  const updating = useAppSelector(state => state.searchConfiguration.updating);
  const updateSuccess = useAppSelector(state => state.searchConfiguration.updateSuccess);

  const handleClose = () => {
    navigate('/search-configuration');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
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
      ...searchConfigurationEntity,
      ...values,
      createdBy: users.find(it => it.id.toString() === values.createdBy?.toString()),
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
          ...searchConfigurationEntity,
          dateCreated: convertDateTimeFromServer(searchConfigurationEntity.dateCreated),
          lastUpdated: convertDateTimeFromServer(searchConfigurationEntity.lastUpdated),
          createdBy: searchConfigurationEntity?.createdBy?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="courageousSearchApp.searchConfiguration.home.createOrEditLabel" data-cy="SearchConfigurationCreateUpdateHeading">
            Create or edit a Search Configuration
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
                <ValidatedField name="id" required readOnly id="search-configuration-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Label"
                id="search-configuration-label"
                name="label"
                data-cy="label"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Description"
                id="search-configuration-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label="Default Config"
                id="search-configuration-defaultConfig"
                name="defaultConfig"
                data-cy="defaultConfig"
                check
                type="checkbox"
              />
              <ValidatedField label="Url" id="search-configuration-url" name="url" data-cy="url" type="text" />
              <ValidatedField
                label="Params Json"
                id="search-configuration-paramsJson"
                name="paramsJson"
                data-cy="paramsJson"
                type="textarea"
              />
              <ValidatedField
                label="Headers Json"
                id="search-configuration-headersJson"
                name="headersJson"
                data-cy="headersJson"
                type="textarea"
              />
              <ValidatedField
                label="Date Created"
                id="search-configuration-dateCreated"
                name="dateCreated"
                data-cy="dateCreated"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label="Last Updated"
                id="search-configuration-lastUpdated"
                name="lastUpdated"
                data-cy="lastUpdated"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField id="search-configuration-createdBy" name="createdBy" data-cy="createdBy" label="Created By" type="select">
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/search-configuration" replace color="info">
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

export default SearchConfigurationUpdate;
