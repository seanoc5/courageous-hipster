import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './content-fragment.reducer';

export const ContentFragmentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const contentFragmentEntity = useAppSelector(state => state.contentFragment.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="contentFragmentDetailsHeading">Content Fragment</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{contentFragmentEntity.id}</dd>
          <dt>
            <span id="label">Label</span>
          </dt>
          <dd>{contentFragmentEntity.label}</dd>
          <dt>
            <span id="text">Text</span>
          </dt>
          <dd>{contentFragmentEntity.text}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{contentFragmentEntity.description}</dd>
          <dt>
            <span id="structuredContent">Structured Content</span>
          </dt>
          <dd>{contentFragmentEntity.structuredContent}</dd>
          <dt>
            <span id="startPos">Start Pos</span>
          </dt>
          <dd>{contentFragmentEntity.startPos}</dd>
          <dt>
            <span id="endPos">End Pos</span>
          </dt>
          <dd>{contentFragmentEntity.endPos}</dd>
          <dt>
            <span id="startTermNum">Start Term Num</span>
          </dt>
          <dd>{contentFragmentEntity.startTermNum}</dd>
          <dt>
            <span id="endTermNum">End Term Num</span>
          </dt>
          <dd>{contentFragmentEntity.endTermNum}</dd>
          <dt>
            <span id="subtype">Subtype</span>
          </dt>
          <dd>{contentFragmentEntity.subtype}</dd>
          <dt>
            <span id="language">Language</span>
          </dt>
          <dd>{contentFragmentEntity.language}</dd>
          <dt>
            <span id="dateCreated">Date Created</span>
          </dt>
          <dd>
            {contentFragmentEntity.dateCreated ? (
              <TextFormat value={contentFragmentEntity.dateCreated} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastUpdated">Last Updated</span>
          </dt>
          <dd>
            {contentFragmentEntity.lastUpdated ? (
              <TextFormat value={contentFragmentEntity.lastUpdated} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>Type</dt>
          <dd>{contentFragmentEntity.type ? contentFragmentEntity.type.id : ''}</dd>
          <dt>Content</dt>
          <dd>{contentFragmentEntity.content ? contentFragmentEntity.content.id : ''}</dd>
          <dt>Context</dt>
          <dd>{contentFragmentEntity.context ? contentFragmentEntity.context.id : ''}</dd>
          <dt>Topic</dt>
          <dd>{contentFragmentEntity.topic ? contentFragmentEntity.topic.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/content-fragment" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/content-fragment/${contentFragmentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ContentFragmentDetail;
