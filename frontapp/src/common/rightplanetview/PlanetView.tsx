import React from 'react';
import {Col, Row} from 'reactstrap';
import {Dispatch} from "redux";
import {connect} from "react-redux";
import {setFocusedPlanet} from "components/actions"
import StoreModel from "StoreModel";
import * as planetPNG from "static/planet.png";

const styles = require('./planetview.module.css');

interface PlanetViewProps {
    planetsData: [any],
    focusedPlanet?: any,
    dispatch: Dispatch<any>;
}

const PlanetView = (props: PlanetViewProps) => {
    return (
        <Row>
            <Col className="col-12 text-center">
                {createPlanetList(props)}
            </Col>
        </Row>
    );
};

function createPlanetList(props: PlanetViewProps) {
    const {planetsData, dispatch, focusedPlanet} = props;
    return planetsData.length > 0 ? planetsData.map(planet =>
        <Row key={planet.id} onClick={() => {
            dispatch(setFocusedPlanet(planet.id))
        }} className={`flex-column ${styles.planetRow}`}>
            <div style={{backgroundImage: `url(${planetPNG})`}} className={styles["planet_image"]}/>
            <p style={focusedPlanet.id === planet.id ? {"color": "#80bdff"} : undefined}>{`Planet ${planet.id}`}</p>
        </Row>) : null;
}

const mapStateToProps = (store: StoreModel) => ({
    planetsData: store.planets.data,
    focusedPlanet: store.planets.focusedPlanet,
});

// @ts-ignore
export default connect(mapStateToProps)(PlanetView)


