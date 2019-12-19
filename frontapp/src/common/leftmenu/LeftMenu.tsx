import React from 'react';
import {useHistory} from "react-router-dom";
import {Col, Row} from 'reactstrap';
import "./LeftMenu.css";

export const LeftMenu = () => {
    const history = useHistory();
        return (
            <Row>
                <Col className="col-12 text-center">
                    <div onClick={() => history.push("/")} className="leftMenuButton">Panel</div>
                </Col>
                <Col className="col-12 text-center">
                    <div onClick={() => history.push("/building")} className="leftMenuButton">Buildings</div>
                </Col>
                <Col className="col-12 text-center">
                    <div onClick={() => history.push("/")} className="leftMenuButton">Docks</div>
                </Col>
                <Col className="col-12 text-center">
                    <div onClick={() => history.push("/")} className="leftMenuButton">Research</div>
                </Col>
                <Col className="col-12 text-center">
                    <div onClick={() => history.push("/fleet")} className="leftMenuButton">Fleet</div>
                </Col>
                <Col className="col-12 text-center">
                    <div onClick={() => history.push("/galaxy")} className="leftMenuButton">Galaxy view</div>
                </Col>
            </Row>
        );
};

export default LeftMenu;


