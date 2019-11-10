import * as React from 'react';
import {ListGroup, ListGroupItem, Row} from 'reactstrap';

export default class FleetPanel extends React.Component {
    public render() {
        return (
            <Row>
                <ListGroup>
                    <ListGroupItem>Cras justo odio</ListGroupItem>
                    <ListGroupItem>Dapibus ac facilisis in</ListGroupItem>
                    <ListGroupItem>Morbi leo risus</ListGroupItem>
                    <ListGroupItem>Porta ac consectetur ac</ListGroupItem>
                    <ListGroupItem>Vestibulum at eros</ListGroupItem>
                </ListGroup>
            </Row>
        );
    }


}
