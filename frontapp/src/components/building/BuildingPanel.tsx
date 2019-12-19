import BuildingGrid from "components/building/BuildingGrid";
import React from 'react';
import {connect} from "react-redux";
import {Col, Row} from "reactstrap";
import {Dispatch} from 'redux';
import StoreModel from "StoreModel";

interface BuildingPanelProps {
    dispatch: Dispatch
}

const BuildingPanel = (props: BuildingPanelProps) => {
    return (
        <Row>
            <Col className="col-6">
                <BuildingGrid/>
            </Col>
            <Col className="col-6"> grid</Col>
        </Row>)
};


const mapStateToProps = (state: StoreModel) => ({});


export default connect(mapStateToProps)(BuildingPanel)

