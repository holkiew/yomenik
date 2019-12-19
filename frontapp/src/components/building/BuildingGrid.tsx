import styles from "components/building/buildinggrid.module.css";
import React, {useState} from 'react';
import {connect} from "react-redux";
import {Dispatch} from 'redux';
import StoreModel from "StoreModel";

interface BuildingGridProps {
    dispatch: Dispatch
}


const BuildingGrid = (props: BuildingGridProps) => {
    const [state, setState] = useState("selectedCell");
    return (
        <div>
            <table>
                <tbody>
                {generateBuildingsGrid(setState)}
                </tbody>
            </table>
        </div>)
};


function generateBuildingsGrid(setState: any): any[] {
    const cols = 5;
    const rows = 2;
    const table = [];
    // TODO:: temporary solution
    const {type, label, imageURL} = availableBuildings.iron_mine;

    const createRow = (row: number): any[] => {
        const tds = [];
        for (let col = 0; col < cols; col++) {
            tds.push(
                <td key={`${row}${col}`} className={styles.table_cell} onClick={() => setState(type)}>
                    <div style={{backgroundImage: `url(${imageURL})`}} className={styles.building_image}/>
                    {label}
                </td>);
        }
        return tds;
    };

    for (let row = 0; row < rows; row++) {
        table.push(
            <tr key={row}>
                {createRow(row)}
            </tr>)
    }
    return table;
}

const availableBuildings = {
    "iron_mine": {
        imageURL: require("static/iron_mine.jpg"),
        label: "Iron mine",
        type: "iron_mine"
    }
};

const mapStateToProps = (state: StoreModel) => ({});

export default connect(mapStateToProps)(BuildingGrid)

