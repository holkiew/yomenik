import React from 'react';
import {ListGroup, ListGroupItem, Row} from 'reactstrap';

interface FleetListProps {
    residingFleet: Map<string, number>
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

    private generateList = (residingFleet: Map<string, number>) => {
        const elementList: any[] = [];
        residingFleet.forEach((value, key) => {
            elementList.push((
                <ListGroupItem key={key}>
                    {`${key}: ${value}`}
                </ListGroupItem>
            ))
        });
        return elementList;
    }

}
