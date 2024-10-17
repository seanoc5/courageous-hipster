import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './thing-type.reducer';

export const ThingTypeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const thingTypeEntity = useAppSelector(state => state.thingType.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="thingTypeDetailsHeading">Thing Type</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{thingTypeEntity.id}</dd>
          <dt>
            <span id="label">Label</span>
          </dt>
          <dd>{thingTypeEntity.label}</dd>
          <dt>
            <span id="parentClass">Parent Class</span>
          </dt>
          <dd>{thingTypeEntity.parentClass}</dd>
          <dt>
            <span id="descrption">Descrption</span>
          </dt>
          <dd>{thingTypeEntity.descrption}</dd>
          <dt>
            <span id="dateCreated">Date Created</span>
          </dt>
          <dd>
            {thingTypeEntity.dateCreated ? <TextFormat value={thingTypeEntity.dateCreated} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="lastUpdated">Last Updated</span>
          </dt>
          <dd>
            {thingTypeEntity.lastUpdated ? <TextFormat value={thingTypeEntity.lastUpdated} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
        </dl>
        <Button tag={Link} to="/thing-type" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/thing-type/${thingTypeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default ThingTypeDetail;
