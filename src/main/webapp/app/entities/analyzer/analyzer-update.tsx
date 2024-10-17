import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row, UncontrolledTooltip } from 'reactstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntities as getContexts } from 'app/entities/context/context.reducer';
import { getEntities as getSearchConfigurations } from 'app/entities/search-configuration/search-configuration.reducer';
import { getEntities as getSearchResults } from 'app/entities/search-result/search-result.reducer';
import { createEntity, getEntity, reset, updateEntity } from './analyzer.reducer';

export const AnalyzerUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const contexts = useAppSelector(state => state.context.entities);
  const searchConfigurations = useAppSelector(state => state.searchConfiguration.entities);
  const searchResults = useAppSelector(state => state.searchResult.entities);
  const analyzerEntity = useAppSelector(state => state.analyzer.entity);
  const loading = useAppSelector(state => state.analyzer.loading);
  const updating = useAppSelector(state => state.analyzer.updating);
  const updateSuccess = useAppSelector(state => state.analyzer.updateSuccess);

  const handleClose = () => {
    navigate('/analyzer');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
    dispatch(getContexts({}));
    dispatch(getSearchConfigurations({}));
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
    values.dateCreated = convertDateTimeToServer(values.dateCreated);
    values.lastUpdated = convertDateTimeToServer(values.lastUpdated);

    const entity = {
      ...analyzerEntity,
      ...values,
      createdBy: users.find(it => it.id.toString() === values.createdBy?.toString()),
      context: contexts.find(it => it.id.toString() === values.context?.toString()),
      searchConfiguration: searchConfigurations.find(it => it.id.toString() === values.searchConfiguration?.toString()),
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
          dateCreated: displayDefaultDateTime(),
          lastUpdated: displayDefaultDateTime(),
        }
      : {
          ...analyzerEntity,
          dateCreated: convertDateTimeFromServer(analyzerEntity.dateCreated),
          lastUpdated: convertDateTimeFromServer(analyzerEntity.lastUpdated),
          createdBy: analyzerEntity?.createdBy?.id,
          context: analyzerEntity?.context?.id,
          searchConfiguration: analyzerEntity?.searchConfiguration?.id,
          searchResult: analyzerEntity?.searchResult?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="courageousSearchApp.analyzer.home.createOrEditLabel" data-cy="AnalyzerCreateUpdateHeading">
            Create or edit a Analyzer
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="analyzer-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Label"
                id="analyzer-label"
                name="label"
                data-cy="label"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField label="Description" id="analyzer-description" name="description" data-cy="description" type="textarea" />
              <ValidatedField label="Code" id="analyzer-code" name="code" data-cy="code" type="text" />
              <UncontrolledTooltip target="codeLabel">closure type groovy code to be run)</UncontrolledTooltip>
              <ValidatedField
                label="Date Created"
                id="analyzer-dateCreated"
                name="dateCreated"
                data-cy="dateCreated"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label="Last Updated"
                id="analyzer-lastUpdated"
                name="lastUpdated"
                data-cy="lastUpdated"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField id="analyzer-createdBy" name="createdBy" data-cy="createdBy" label="Created By" type="select">
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="analyzer-context" name="context" data-cy="context" label="Context" type="select">
                <option value="" key="0" />
                {contexts
                  ? contexts.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="analyzer-searchConfiguration"
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
              <ValidatedField id="analyzer-searchResult" name="searchResult" data-cy="searchResult" label="Search Result" type="select">
                <option value="" key="0" />
                {searchResults
                  ? searchResults.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/analyzer" replace color="info">
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

export default AnalyzerUpdate;
