import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './context.reducer';

export const ContextDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const contextEntity = useAppSelector(state => state.context.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="contextDetailsHeading">Context</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{contextEntity.id}</dd>
          <dt>
            <span id="label">Label</span>
          </dt>
          <dd>{contextEntity.label}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{contextEntity.description}</dd>
          <dt>
            <span id="level">Level</span>
          </dt>
          <dd>{contextEntity.level}</dd>
          <dt>
            <span id="time">Time</span>
          </dt>
          <dd>{contextEntity.time}</dd>
          <dt>
            <span id="location">Location</span>
          </dt>
          <dd>{contextEntity.location}</dd>
          <dt>
            <span id="intent">Intent</span>
          </dt>
          <dd>{contextEntity.intent}</dd>
          <dt>
            <span id="defaultContext">Default Context</span>
          </dt>
          <dd>{contextEntity.defaultContext ? 'true' : 'false'}</dd>
          <dt>
            <span id="dateCreated">Date Created</span>
          </dt>
          <dd>
            {contextEntity.dateCreated ? <TextFormat value={contextEntity.dateCreated} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="lastUpdate">Last Update</span>
          </dt>
          <dd>{contextEntity.lastUpdate ? <TextFormat value={contextEntity.lastUpdate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>Created By</dt>
          <dd>{contextEntity.createdBy ? contextEntity.createdBy.id : ''}</dd>
          <dt>Organization</dt>
          <dd>{contextEntity.organization ? contextEntity.organization.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/context" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/context/${contextEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ContextDetail;
