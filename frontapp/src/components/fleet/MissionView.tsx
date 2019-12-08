import React, {useEffect} from "react"
import {connect} from "react-redux";
import {Button, Col, Form, FormGroup, Input, Label, Row} from "reactstrap";
import {Dispatch} from "redux";
import StoreModel from "StoreModel";
import {sendFleetOnMission, setMissionPlanetCoords, setMissionPlanetTo, setMissionType} from "./actions";
import {MissionType} from "./MissionType";
import styles from "./missionview.module.css"

interface MissionViewProps {
    dispatch: Dispatch,
    missionType: MissionType,
    planetIdTo: string,
    planetCoordinates: { x: string, y: string }
}

const MissionView = (props: MissionViewProps) => {
    const {dispatch, missionType, planetIdTo, planetCoordinates} = props;
    useEffect(() => () => componentWillUnmount(props), []);
    return (
        <Form onSubmit={(e) => {
            dispatch(sendFleetOnMission());
            e.preventDefault();
        }}>
            <Row>
                <Col className="col-3">
                    <FormGroup>
                        <Label>Target Coordinates</Label>
                        <Col className="col-8">
                            <Input type="number" defaultValue={planetCoordinates.x} placeholder="x"/>
                            <Input type="number" defaultValue={planetCoordinates.y} placeholder="y"/>
                        </Col>
                    </FormGroup>
                </Col>
                <Col className="col-3">
                    <FormGroup>
                        <Label>(Or planetId)</Label>
                        <Col className="col-8">
                            <Input type="number" placeholder="id" defaultValue={planetIdTo} onBlur={e => dispatch(setMissionPlanetTo(e.target.value))}/>
                        </Col>
                    </FormGroup>
                </Col>
            </Row>
            <Row>
                <Col className="col-3">
                    <FormGroup>
                        <Label for="exampleSelect">Mission type</Label>
                        <Input type="select" name="select" className={styles.missionOptionsInput} defaultValue={missionType}
                               onChange={e => dispatch(setMissionType(e.target.value as MissionType))}>
                            <option value={MissionType.NONE} disabled hidden>Choose mission</option>
                            <option value={MissionType.MOVE}>Move</option>
                            <option value={MissionType.ATTACK}>Attack</option>
                            <option value={MissionType.TRANSFER}>Transport</option>
                        </Input>
                    </FormGroup>
                </Col>
            </Row>
            <FormGroup>
                <Button color="success">Send on mission</Button>
            </FormGroup>
        </Form>
    )
};

function componentWillUnmount(props: MissionViewProps) {
    const {dispatch} = props;
    dispatch(setMissionType(MissionType.NONE));
    dispatch(setMissionPlanetTo(""));
    dispatch(setMissionPlanetCoords('', ''));
}

const mapStateToProps = (state: StoreModel) => ({
    missionType: state.fleets.missionType,
    planetIdTo: state.fleets.planetIdTo,
    planetCoordinates: state.fleets.planetCoordinates
});

export default connect(mapStateToProps)(MissionView)