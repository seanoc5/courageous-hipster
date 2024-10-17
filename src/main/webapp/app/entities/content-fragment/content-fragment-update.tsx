import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getThingTypes } from 'app/entities/thing-type/thing-type.reducer';
import { getEntities as getContents } from 'app/entities/content/content.reducer';
import { getEntities as getContexts } from 'app/entities/context/context.reducer';
import { getEntities as getTopics } from 'app/entities/topic/topic.reducer';
import { createEntity, getEntity, reset, updateEntity } from './content-fragment.reducer';

export const ContentFragmentUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const thingTypes = useAppSelector(state => state.thingType.entities);
  const contents = useAppSelector(state => state.content.entities);
  const contexts = useAppSelector(state => state.context.entities);
  const topics = useAppSelector(state => state.topic.entities);
  const contentFragmentEntity = useAppSelector(state => state.contentFragment.entity);
  const loading = useAppSelector(state => state.contentFragment.loading);
  const updating = useAppSelector(state => state.contentFragment.updating);
  const updateSuccess = useAppSelector(state => state.contentFragment.updateSuccess);

  const handleClose = () => {
    navigate('/content-fragment');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getThingTypes({}));
    dispatch(getContents({}));
    dispatch(getContexts({}));
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
    if (values.startPos !== undefined && typeof values.startPos !== 'number') {
      values.startPos = Number(values.startPos);
    }
    if (values.endPos !== undefined && typeof values.endPos !== 'number') {
      values.endPos = Number(values.endPos);
    }
    if (values.startTermNum !== undefined && typeof values.startTermNum !== 'number') {
      values.startTermNum = Number(values.startTermNum);
    }
    if (values.endTermNum !== undefined && typeof values.endTermNum !== 'number') {
      values.endTermNum = Number(values.endTermNum);
    }
    values.dateCreated = convertDateTimeToServer(values.dateCreated);
    values.lastUpdated = convertDateTimeToServer(values.lastUpdated);

    const entity = {
      ...contentFragmentEntity,
      ...values,
      type: thingTypes.find(it => it.id.toString() === values.type?.toString()),
      content: contents.find(it => it.id.toString() === values.content?.toString()),
      context: contexts.find(it => it.id.toString() === values.context?.toString()),
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
          ...contentFragmentEntity,
          dateCreated: convertDateTimeFromServer(contentFragmentEntity.dateCreated),
          lastUpdated: convertDateTimeFromServer(contentFragmentEntity.lastUpdated),
          type: contentFragmentEntity?.type?.id,
          content: contentFragmentEntity?.content?.id,
          context: contentFragmentEntity?.context?.id,
          topic: contentFragmentEntity?.topic?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="courageousSearchApp.contentFragment.home.createOrEditLabel" data-cy="ContentFragmentCreateUpdateHeading">
            Create or edit a Content Fragment
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
                <ValidatedField name="id" required readOnly id="content-fragment-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label="Label"
                id="content-fragment-label"
                name="label"
                data-cy="label"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField label="Text" id="content-fragment-text" name="text" data-cy="text" type="textarea" />
              <ValidatedField
                label="Description"
                id="content-fragment-description"
                name="description"
                data-cy="description"
                type="textarea"
              />
              <ValidatedField
                label="Structured Content"
                id="content-fragment-structuredContent"
                name="structuredContent"
                data-cy="structuredContent"
                type="textarea"
              />
              <ValidatedField label="Start Pos" id="content-fragment-startPos" name="startPos" data-cy="startPos" type="text" />
              <ValidatedField label="End Pos" id="content-fragment-endPos" name="endPos" data-cy="endPos" type="text" />
              <ValidatedField
                label="Start Term Num"
                id="content-fragment-startTermNum"
                name="startTermNum"
                data-cy="startTermNum"
                type="text"
              />
              <ValidatedField label="End Term Num" id="content-fragment-endTermNum" name="endTermNum" data-cy="endTermNum" type="text" />
              <ValidatedField label="Subtype" id="content-fragment-subtype" name="subtype" data-cy="subtype" type="text" />
              <ValidatedField label="Language" id="content-fragment-language" name="language" data-cy="language" type="text" />
              <ValidatedField
                label="Date Created"
                id="content-fragment-dateCreated"
                name="dateCreated"
                data-cy="dateCreated"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label="Last Updated"
                id="content-fragment-lastUpdated"
                name="lastUpdated"
                data-cy="lastUpdated"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField id="content-fragment-type" name="type" data-cy="type" label="Type" type="select">
                <option value="" key="0" />
                {thingTypes
                  ? thingTypes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="content-fragment-content" name="content" data-cy="content" label="Content" type="select">
                <option value="" key="0" />
                {contents
                  ? contents.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="content-fragment-context" name="context" data-cy="context" label="Context" type="select">
                <option value="" key="0" />
                {contexts
                  ? contexts.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="content-fragment-topic" name="topic" data-cy="topic" label="Topic" type="select">
                <option value="" key="0" />
                {topics
                  ? topics.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/content-fragment" replace color="info">
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

export default ContentFragmentUpdate;
