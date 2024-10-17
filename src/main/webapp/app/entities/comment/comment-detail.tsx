import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './comment.reducer';

export const CommentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const commentEntity = useAppSelector(state => state.comment.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="commentDetailsHeading">Comment</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{commentEntity.id}</dd>
          <dt>
            <span id="label">Label</span>
          </dt>
          <dd>{commentEntity.label}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{commentEntity.description}</dd>
          <dt>
            <span id="url">Url</span>
          </dt>
          <dd>{commentEntity.url}</dd>
          <dt>
            <span id="dateCreated">Date Created</span>
          </dt>
          <dd>
            {commentEntity.dateCreated ? <TextFormat value={commentEntity.dateCreated} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="lastUpdate">Last Update</span>
          </dt>
          <dd>{commentEntity.lastUpdate ? <TextFormat value={commentEntity.lastUpdate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>User</dt>
          <dd>{commentEntity.user ? commentEntity.user.id : ''}</dd>
          <dt>Content</dt>
          <dd>{commentEntity.content ? commentEntity.content.id : ''}</dd>
          <dt>Content Fragment</dt>
          <dd>{commentEntity.contentFragment ? commentEntity.contentFragment.id : ''}</dd>
          <dt>Context</dt>
          <dd>{commentEntity.context ? commentEntity.context.id : ''}</dd>
          <dt>Organization</dt>
          <dd>{commentEntity.organization ? commentEntity.organization.id : ''}</dd>
          <dt>Search</dt>
          <dd>{commentEntity.search ? commentEntity.search.id : ''}</dd>
          <dt>Search Configuration</dt>
          <dd>{commentEntity.searchConfiguration ? commentEntity.searchConfiguration.id : ''}</dd>
          <dt>Search Result</dt>
          <dd>{commentEntity.searchResult ? commentEntity.searchResult.id : ''}</dd>
          <dt>Tag</dt>
          <dd>{commentEntity.tag ? commentEntity.tag.id : ''}</dd>
          <dt>Thing Type</dt>
          <dd>{commentEntity.thingType ? commentEntity.thingType.id : ''}</dd>
          <dt>Topic</dt>
          <dd>{commentEntity.topic ? commentEntity.topic.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/comment" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/comment/${commentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default CommentDetail;
