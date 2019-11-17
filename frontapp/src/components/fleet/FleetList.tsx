import React, {useState} from 'react';
import {FormFeedback, FormGroup, Input, Label, ListGroup, ListGroupItem, Row} from 'reactstrap';
import StoreModel from "StoreModel";
import {connect} from "react-redux";
import {Dispatch} from "redux";
import {setSelectedFleet} from "./actions";

interface FleetListProps {
    residingFleet: Map<string, number>
    dispatch: Dispatch
}

const FleetPanel = (props: FleetListProps) => {
    const {residingFleet, dispatch} = props;
    return (
        <Row>
            <ListGroup>
                {generateList(residingFleet, dispatch)}
            </ListGroup>
        </Row>
    );
};

function generateList(residingFleet: Map<string, number>, dispatch: Dispatch) {
    const elementList: any[] = [];
    residingFleet.forEach((residingFleetAmount, templateName) => {
        const [inputValue, inputSetValue] = useState('');
        const isInvalid = () => Number(inputValue) > residingFleetAmount;
        elementList.push((
            <ListGroupItem key={templateName}>
                <FormGroup>
                    <Label>{`${templateName}: ${residingFleetAmount}`}</Label>
                    <Input placeholder="amount" value={inputValue}
                           onChange={(e) => inputSetValue(e.target.value.replace(/\D+/g, ''))}
                           invalid={isInvalid()}
                           onBlur={() => dispatch(setSelectedFleet(templateName, isInvalid() ? 0 : Number(inputValue)))}
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

