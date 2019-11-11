import React from 'react';
import {ListGroup, ListGroupItem, Row} from 'reactstrap';

interface FleetListProps {
    residingFleet: object
}

export default class FleetPanel extends React.Component<FleetListProps> {
    public render() {
        const {residingFleet} = this.props;
        return (
            <Row>
                <ListGroup>
                    {this.generateList(residingFleet)}
                </ListGroup>
            </Row>
        );
    }

    private generateList = (residingFleet: object) => {
        const elementList: any[] = [];
        for (let key in residingFleet) {
            elementList.push((
                <ListGroupItem key={key}>
                    {`${key}: ${residingFleet[key]}`}
                </ListGroupItem>
            ))
        }
        return elementList;
    }

}
