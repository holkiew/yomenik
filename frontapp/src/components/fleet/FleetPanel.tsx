import {toggleNewTemplateModal} from "components/fleet/actions";
import TemplateModal from "components/fleet/TemplateModal";
import React from 'react';
import {connect} from "react-redux";
import {Button, Col, Row} from 'reactstrap';
import {Dispatch} from "redux";
import StoreModel from "StoreModel";
import FleetList from "./FleetList";
import MissionView from "./MissionView";

interface FleetPanelProps {
    focusedPlanet?: any,
    dispatch: Dispatch
}

const FleetPanel = (props: FleetPanelProps) => {
    const {dispatch} = props;
    return (
        <div>
            <Row>
                <Col className="col-xs-12 col-sm-8 col-md-4 col-lg-3">
                    <FleetList/>
                </Col>
                <Col className="col-xs-12 col-sm-4 col-md-8 col-lg-9">
                    <MissionView/>
                    <Button color="primary" onClick={() => dispatch(toggleNewTemplateModal())}>New Template</Button>
                    <Button color="primary">New Mission</Button>
                    <TemplateModal/>
                </Col>
            </Row>
        </div>
    );
};

const mapStateToProps = (state: StoreModel) => (
    {focusedPlanet: state.planets.focusedPlanet}
);

export default connect(mapStateToProps)(FleetPanel)

