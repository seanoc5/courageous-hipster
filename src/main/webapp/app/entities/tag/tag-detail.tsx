import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './tag.reducer';

export const TagDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const tagEntity = useAppSelector(state => state.tag.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="tagDetailsHeading">Tag</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{tagEntity.id}</dd>
          <dt>
            <span id="label">Label</span>
          </dt>
          <dd>{tagEntity.label}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{tagEntity.description}</dd>
          <dt>
            <span id="defaultTag">Default Tag</span>
          </dt>
          <dd>{tagEntity.defaultTag ? 'true' : 'false'}</dd>
          <dt>
            <span id="dateCreated">Date Created</span>
          </dt>
          <dd>{tagEntity.dateCreated ? <TextFormat value={tagEntity.dateCreated} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="lastUpdated">Last Updated</span>
          </dt>
          <dd>{tagEntity.lastUpdated ? <TextFormat value={tagEntity.lastUpdated} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>Created By</dt>
          <dd>{tagEntity.createdBy ? tagEntity.createdBy.id : ''}</dd>
          <dt>Analyzer</dt>
          <dd>{tagEntity.analyzer ? tagEntity.analyzer.id : ''}</dd>
          <dt>Content</dt>
          <dd>{tagEntity.content ? tagEntity.content.id : ''}</dd>
          <dt>Content Fragment</dt>
          <dd>{tagEntity.contentFragment ? tagEntity.contentFragment.id : ''}</dd>
          <dt>Context</dt>
          <dd>{tagEntity.context ? tagEntity.context.id : ''}</dd>
          <dt>Organization</dt>
          <dd>{tagEntity.organization ? tagEntity.organization.id : ''}</dd>
          <dt>Search</dt>
          <dd>{tagEntity.search ? tagEntity.search.id : ''}</dd>
          <dt>Search Configuration</dt>
          <dd>{tagEntity.searchConfiguration ? tagEntity.searchConfiguration.id : ''}</dd>
          <dt>Search Result</dt>
          <dd>{tagEntity.searchResult ? tagEntity.searchResult.id : ''}</dd>
          <dt>Thing Type</dt>
          <dd>{tagEntity.thingType ? tagEntity.thingType.id : ''}</dd>
          <dt>Topic</dt>
          <dd>{tagEntity.topic ? tagEntity.topic.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/tag" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/tag/${tagEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default TagDetail;
