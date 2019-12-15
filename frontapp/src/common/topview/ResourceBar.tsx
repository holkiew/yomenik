import ResourceCell from "common/topview/ResourceCell";
import WarningCell from "common/topview/WarningCell";
import React from 'react';
import {connect} from "react-redux";
import {Col, Row} from 'reactstrap';
import {Dispatch} from 'redux';
import StoreModel from "StoreModel";
import styles from "./resourcebar.module.css"


interface ResourceBarProps {
    dispatch: Dispatch
    resources?: { [resourceType: string]: any }
    planetsData: any
}

const ResourceBar = (props: ResourceBarProps) => {
    const {resources} = props;
    return (
        <Row>
            <Col className="offset-3 col-6">
                <table className={styles.resource_table}>
                    <tr>
                        {resources &&
                        <td className={styles.td_marker}>
                            <ResourceCell {...resources?.iron}/>
                        </td>}
                    </tr>
                </table>
            </Col>
            <Col className="offset-1 col-1">
                <WarningCell {...props}/>
            </Col>
        </Row>);
};

const mapStateToProps = (state: StoreModel) => ({
    resources: state.planets.focusedPlanet?.resources,
    planetsData: state.planets.data
});

export default connect(mapStateToProps)(ResourceBar)

