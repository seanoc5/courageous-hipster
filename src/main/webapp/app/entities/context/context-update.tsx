import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntities as getOrganizations } from 'app/entities/organization/organization.reducer';
import { createEntity, getEntity, reset, updateEntity } from './context.reducer';

export const ContextUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const organizations = useAppSelector(state => state.organization.entities);
  const contextEntity = useAppSelector(state => state.context.entity);
  const loading = useAppSelector(state => state.context.loading);
  const updating = useAppSelector(state => state.context.updating);
  const updateSuccess = useAppSelector(state => state.context.updateSuccess);

  const handleClose = () => {
    navigate('/context');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
    dispatch(getOrganizations({}));
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
      ...contextEntity,
      ...values,
      createdBy: users.find(it => it.id.toString() === values.createdBy?.toString()),
      organization: organizations.find(it => it.id.toString() === values.organization?.toString()),
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
          ...contextEntity,
          dateCreated: convertDateTimeFromServer(contextEntity.dateCreated),
          lastUpdate: convertDateTimeFromServer(contextEntity.lastUpdate),
          createdBy: contextEntity?.createdBy?.id,
          organization: contextEntity?.organization?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="courageousSearchApp.context.home.createOrEditLabel" data-cy="ContextCreateUpdateHeading">
            Create or edit a Context
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="context-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Label"
                id="context-label"
                name="label"
                data-cy="label"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField label="Description" id="context-description" name="description" data-cy="description" type="text" />
              <ValidatedField label="Level" id="context-level" name="level" data-cy="level" type="text" />
              <ValidatedField label="Time" id="context-time" name="time" data-cy="time" type="text" />
              <ValidatedField label="Location" id="context-location" name="location" data-cy="location" type="text" />
              <ValidatedField label="Intent" id="context-intent" name="intent" data-cy="intent" type="text" />
              <ValidatedField
                label="Default Context"
                id="context-defaultContext"
                name="defaultContext"
                data-cy="defaultContext"
                check
                type="checkbox"
              />
              <ValidatedField
                label="Date Created"
                id="context-dateCreated"
                name="dateCreated"
                data-cy="dateCreated"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label="Last Update"
                id="context-lastUpdate"
                name="lastUpdate"
                data-cy="lastUpdate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField id="context-createdBy" name="createdBy" data-cy="createdBy" label="Created By" type="select">
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="context-organization" name="organization" data-cy="organization" label="Organization" type="select">
                <option value="" key="0" />
                {organizations
                  ? organizations.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/context" replace color="info">
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

export default ContextUpdate;
