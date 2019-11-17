import React from "react"
import {Button, Col, Form, FormGroup, Input, Label, Row} from "reactstrap";
import {MissionType} from "./MissionType";
import {Dispatch} from "redux";
import {connect} from "react-redux";
import {sendFleetOnMission, setMissionPlanetTo, setMissionType} from "./actions";

interface MissionViewProps {
    dispatch: Dispatch
}

const MissionView = (props: MissionViewProps) => {
    const {dispatch} = props;
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
                            <Input type="number" placeholder="x"/>
                            <Input type="number" placeholder="y"/>
                        </Col>
                    </FormGroup>
                </Col>
                <Col className="col-3">
                    <FormGroup>
                        <Label>(Or planetId)</Label>
                        <Col className="col-8">
                            <Input type="number" placeholder="id"
                                   onBlur={e => dispatch(setMissionPlanetTo(e.target.value))}/>
                        </Col>
                    </FormGroup>
                </Col>
            </Row>
            <Row>
                <Col className="col-3">
                    <FormGroup>
                        <Label for="exampleSelect">Mission type</Label>
                        <Input type="select" name="select" id="exampleSelect"
                               onChange={e => dispatch(setMissionType(e.target.value as MissionType))}>
                            <option selected disabled hidden>Choose mission</option>
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

const mapStateToProps = () => ({});

export default connect(mapStateToProps)(MissionView)