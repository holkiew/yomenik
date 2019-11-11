import React from 'react';
import {Col, Row} from "reactstrap";
import Axios from "axios-observable";
import * as env from "config.json";
import {setPlanetsData} from "components/actions";
import {Dispatch} from 'redux';
import {connect} from 'react-redux';
import StoreModel from "StoreModel";

interface HomePanelProps {
    focusedPlanet?: any,
    dispatch: Dispatch<any>;
}

class HomePanel extends React.Component<HomePanelProps> {

    constructor(props: HomePanelProps) {
        super(props);
        this.getUserPlanets()
    }

    public render() {
        const {focusedPlanet} = this.props;
        return (
            <Row>
                <Col className="col-12 text-center">
                    <div>{`Planet ${focusedPlanet ? focusedPlanet.id : "loading"}`}</div>
                </Col>
            </Row>
        );
    }

    private getUserPlanets = () => {
        Axios.get(`${env.backendServer.baseUrl}${env.backendServer.services.planet}/planets`)
            .subscribe(response => {
                this.props.dispatch(setPlanetsData(response.data))
            });
    };
}

const mapStateToProps = (store: StoreModel) => ({
    focusedPlanet: store.planets.focusedPlanet
});

export default connect(mapStateToProps)(HomePanel);

