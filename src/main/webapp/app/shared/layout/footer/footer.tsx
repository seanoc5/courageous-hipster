import './footer.scss';

import React from 'react';

import { Col, Row } from 'reactstrap';

const Footer = () => {
  const currentTime = new Date().toLocaleTimeString();

  return (
    <div className="footer page-content">
      <Row>
        <Col md="12" className="text-center">
          <div>
            Rendered at <span>{currentTime}</span>
          </div>
          <div>be courageous!</div>
        </Col>
      </Row>
    </div>
  );
};

export default Footer;
