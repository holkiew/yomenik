import * as React from 'react';
import {Col, Row} from 'reactstrap';

export const PlanetView = () => {
    return (
        <Row>
            <Col className="col-12 text-center">
                <div onClick={() => {
                }} className="">Planet1
                </div>
                <div onClick={() => {
                }} className="">Planet2
                </div>
                <div onClick={() => {
                }} className="">Planet3
                </div>
            </Col>
        </Row>
    );
};

export default PlanetView;


