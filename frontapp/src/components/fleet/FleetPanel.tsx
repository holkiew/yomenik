import React from 'react';
import {Button, Col, Row} from 'reactstrap';
import {Dispatch} from "redux";
import FleetList from "./FleetList";
import {connect} from "react-redux";
import StoreModel from "StoreModel";
import MissionView from "./MissionView";

interface FleetPanelProps {
    focusedPlanet?: any,
    dispatch: Dispatch
}

const FleetPanel = (props: FleetPanelProps) => {
    return (
        <div>
            <Row>
                <Col className="col-xs-12 col-sm-8 col-md-4 col-lg-3">
                    <FleetList/>
                </Col>
                <Col className="col-xs-12 col-sm-4 col-md-8 col-lg-9">
                    <MissionView/>
                    <Button color="primary">New Template</Button>
                    <Button color="primary">New Mission</Button>
                </Col>
            </Row>
        </div>
    );
};

const mapStateToProps = (state: StoreModel) => (
    {focusedPlanet: state.planets.focusedPlanet}
);

export default connect(mapStateToProps)(FleetPanel)

