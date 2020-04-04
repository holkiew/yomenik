import {getAvailableTemplatesRequest, toggleListTemplatesModal, toggleNewTemplateModal} from "components/fleet/actions";
import ListTemplatesModal from "components/fleet/ListTemplatesModal";
import NewTemplateModal from "components/fleet/NewTemplateModal";
import React, {useEffect} from 'react';
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
    useEffect(() => {
        componentDidMount(props);
    }, []);
    return (
        <div>
            <Row>
                <Col className="col-xs-12 col-sm-8 col-md-4 col-lg-3">
                    <FleetList/>
                </Col>
                <Col className="col-xs-12 col-sm-4 col-md-8 col-lg-9">
                    <MissionView/>
                    <Button color="primary" onClick={() => dispatch(toggleNewTemplateModal())}>New Template</Button>
                    <div style={{margin: "10px"}}/>
                    <Button color="warning" onClick={() => dispatch(toggleListTemplatesModal())}>Check templates</Button>
                    <div style={{margin: "10px"}}/>
                    <NewTemplateModal/>
                    <ListTemplatesModal/>
                </Col>
            </Row>
        </div>
    );
};

function componentDidMount(props: FleetPanelProps) {
    const {dispatch} = props;
    dispatch(getAvailableTemplatesRequest());
}

const mapStateToProps = (state: StoreModel) => (
    {focusedPlanet: state.planets.focusedPlanet}
);

export default connect(mapStateToProps)(FleetPanel)

