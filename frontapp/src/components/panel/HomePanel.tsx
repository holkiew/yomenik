import React from 'react';
import {Col, Row} from "reactstrap";
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
}

const mapStateToProps = (state: StoreModel) => ({
    focusedPlanet: state.planets.focusedPlanet
});

export default connect(mapStateToProps)(HomePanel);

