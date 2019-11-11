import React from 'react';
import {Button, Col, Row} from 'reactstrap';
import {Dispatch} from "redux";
import StoreModel from "StoreModel";
import {connect} from "react-redux";
import FleetList from "./FleetList";

interface FleetPanelProps {
    focusedPlanet?: any,
    dispatch: Dispatch
}

class FleetPanel extends React.Component<FleetPanelProps> {
    public render() {
        const {focusedPlanet} = this.props;
        return (
            <div>
                <Row>
                    <Col className="col-xs-12 col-sm-8 col-md-4 col-lg-3">
                        <FleetList residingFleet={focusedPlanet ? focusedPlanet.residingFleet : {}}/>
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

const mapStateToProps = (store: StoreModel) => ({
    focusedPlanet: store.planets.focusedPlanet
});

export default connect(mapStateToProps)(FleetPanel)

