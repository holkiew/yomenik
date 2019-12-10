import ResourceCell from "common/topview/ResourceCell";
import React from 'react';
import {connect} from "react-redux";
import {Dispatch} from 'redux';
import StoreModel from "StoreModel";
import styles from "./resourcebar.module.css"


interface ResourceBarProps {
    dispatch: Dispatch
    resources?: { [resourceType: string]: any }
}

const ResourceBar = (props: ResourceBarProps) => {
    const {resources} = props;
    return (
        <div>
            {resources &&
            <table className={styles.resource_table}>
                <tr>
                    <td>
                        <ResourceCell {...resources.iron}/>
                    </td>
                </tr>
            </table>}

        </div>);
};

const mapStateToProps = (state: StoreModel) => ({
    resources: state.planets.focusedPlanet ? state.planets.focusedPlanet.resources : undefined
});

export default connect(mapStateToProps)(ResourceBar)

