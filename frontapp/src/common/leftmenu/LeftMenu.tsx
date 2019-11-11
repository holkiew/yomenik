import React from 'react';
import {Col, Row} from 'reactstrap';
import {useHistory} from "react-router-dom";
import "./LeftMenu.css";

export const LeftMenu = () => {
    const history = useHistory();
        return (
            <Row>
                <Col className="col-12 text-center">
                    <div onClick={() => history.push("/")} className="leftMenuButton">Panel</div>
                </Col>
                <Col className="col-12 text-center">
                    <div onClick={() => history.push("/")} className="leftMenuButton">Buildings</div>
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
            </Row>
        );
};

export default LeftMenu;


