import React from 'react';
import {Dispatch} from 'redux';
import StoreModel from "StoreModel";
import {connect} from "react-redux";
import styles from "./resourcebar.module.css"
import * as SteelBarsPNG from "static/steel_bars.jpeg";


interface ResourceBarProps {
    dispatch: Dispatch
    resources?: { [resourceType: string]: { amount: number } }
}

const ResourceBar = (props: ResourceBarProps) => {
    const {resources} = props;
    return (
        <div>
            <table className={styles.resource_table}>
                <tr>
                    <td>
                        Iron {resources && resources.iron.amount}
                        <div style={{backgroundImage: `url(${SteelBarsPNG})`}} className={styles.planet_image}/>
                    </td>
                </tr>
            </table>
        </div>);
};

const mapStateToProps = (state: StoreModel) => ({
    resources: state.planets.focusedPlanet ? state.planets.focusedPlanet.resources : undefined
});

export default connect(mapStateToProps)(ResourceBar)

