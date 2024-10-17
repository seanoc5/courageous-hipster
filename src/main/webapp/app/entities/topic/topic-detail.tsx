import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './topic.reducer';

export const TopicDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const topicEntity = useAppSelector(state => state.topic.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="topicDetailsHeading">Topic</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{topicEntity.id}</dd>
          <dt>
            <span id="label">Label</span>
          </dt>
          <dd>{topicEntity.label}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{topicEntity.description}</dd>
          <dt>
            <span id="defaultTopic">Default Topic</span>
          </dt>
          <dd>{topicEntity.defaultTopic ? 'true' : 'false'}</dd>
          <dt>
            <span id="level">Level</span>
          </dt>
          <dd>{topicEntity.level}</dd>
          <dt>
            <span id="dateCreated">Date Created</span>
          </dt>
          <dd>{topicEntity.dateCreated ? <TextFormat value={topicEntity.dateCreated} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="lastUpdated">Last Updated</span>
          </dt>
          <dd>{topicEntity.lastUpdated ? <TextFormat value={topicEntity.lastUpdated} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>Created By</dt>
          <dd>{topicEntity.createdBy ? topicEntity.createdBy.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/topic" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/topic/${topicEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default TopicDetail;
