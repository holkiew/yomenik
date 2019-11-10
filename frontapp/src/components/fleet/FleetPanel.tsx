import * as React from 'react';
import {Button, Col, ListGroup, ListGroupItem, Row} from 'reactstrap';

export default class FleetPanel extends React.Component {
    public render() {
        return (
            <div>
                <Row>
                    <Col className="col-xs-12 col-sm-8 col-md-4 col-lg-3">
                        <ListGroup>
                            <ListGroupItem>Cras justo odio</ListGroupItem>
                            <ListGroupItem>Dapibus ac facilisis in</ListGroupItem>
                            <ListGroupItem>Morbi leo risus</ListGroupItem>
                            <ListGroupItem>Porta ac consectetur ac</ListGroupItem>
                            <ListGroupItem>Vestibulum at eros</ListGroupItem>
                        </ListGroup>
                    </Col>
                    <Col className="col-xs-12 col-sm-4 col-md-8 col-lg-9">
                        <Button color="primary">New Template</Button>
                        <Button color="primary">New Mission</Button>
                    </Col>
                </Row>
            </div>
        );
    }
}
