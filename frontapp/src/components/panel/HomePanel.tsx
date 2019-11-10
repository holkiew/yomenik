import * as React from 'react';
import {Col, Row} from "reactstrap";
import Axios from "axios-observable";
import * as env from "../../config.json";


export default class HomePanel extends React.Component<any, any> {

    public render() {
        this.getUserPlanets()
        return (
            <Row>
                <Col className="col-12 text-center">
                    <div>planet1</div>
                </Col>
            </Row>
        );
    }

    private getUserPlanets = () => {
        Axios.get(`${env.backendServer.baseUrl}${env.backendServer.services.planet}/planets`)
            .subscribe(value => {
                console.info(value)
            });
    };

}
