import React, {useState} from 'react';
import {connect} from "react-redux";
import {FormFeedback, FormGroup, Input, Label, ListGroup, ListGroupItem, Row} from 'reactstrap';
import {Dispatch} from "redux";
import StoreModel from "StoreModel";
import {setSelectedFleet} from "./actions";

interface FleetListProps {
    residingFleet: Map<string, number>
    dispatch: Dispatch
}

const FleetPanel = (props: FleetListProps) => {
    const {residingFleet, dispatch} = props;
    const [stateObject, setState] = useState({});
    return (
        <Row>
            <ListGroup>
                {generateList(residingFleet, dispatch, [stateObject, setState])}
            </ListGroup>
        </Row>
    );
};

function generateList(residingFleet: Map<string, number>, dispatch: Dispatch, useState: [{}, React.Dispatch<React.SetStateAction<{}>>]) {
    const elementList: any[] = [];
    const [stateObject, setState] = useState;
    residingFleet.forEach((residingFleetAmount, templateName) => {
        const isInvalid = () => Number(stateObject[templateName]) > residingFleetAmount;
        elementList.push((
            <ListGroupItem key={templateName}>
                <FormGroup>
                    <Label>{`${templateName}: ${residingFleetAmount}`}</Label>
                    <Input placeholder="amount" value={stateObject[templateName]}
                           onChange={(e) => setState({
                               ...stateObject,
                               [templateName]: e.target.value.replace(/\D+/g, '')
                           })}
                           invalid={isInvalid()}
                           onBlur={() => dispatch(setSelectedFleet(templateName, isInvalid() ? 0 : Number(stateObject[templateName])))}
                    />
                    <FormFeedback>Exceeding maximum available amount</FormFeedback>
                </FormGroup>
            </ListGroupItem>
        ))
    });
    return elementList;
}

const mapStateToProps = (state: StoreModel) => {
    const {focusedPlanet} = state.planets;
    return {residingFleet: new Map<string, number>(Object.entries(focusedPlanet ? focusedPlanet.residingFleet : {}))};
};

export default connect(mapStateToProps)(FleetPanel)

