import styles from "components/building/planetbuildinggrid.module.css";
import React from 'react';
import {connect} from "react-redux";
import {Dispatch} from 'redux';
import StoreModel from "StoreModel";
import {clearSelectedOptionSlot, setSelectedPlanetSlot} from "./actions"

interface PlanetBuildingGridProps {
    dispatch: Dispatch,
    selectedBuildingSlot: {
        slotKey: string
    }
}

const PlanetBuildingGrid = (props: PlanetBuildingGridProps) => {
    console.info(props)
    return (
        <div>
            <table>
                <tbody>
                {generateBuildingsGrid(props)}
                </tbody>
            </table>
        </div>)
};

function generateBuildingsGrid(props: PlanetBuildingGridProps): any[] {
    const {dispatch, selectedBuildingSlot: {slotKey}} = props;
    const cols = 3;
    const rows = 1;
    const table = [];

    const createRow = (row: number): any[] => {
        const tds = [];
        for (let col = 0; col < cols; col++) {
            const chosenBuilding = planetBuildings[(row * cols) + col + 1];
            const {occupied, imageURL, label} = chosenBuilding;
            const colRow = `${row}${col}`;
            tds.push(
                <td key={colRow} className={`${styles.table_cell} ${slotKey === colRow && styles.table_cell_selected}`} onClick={() => onCellClick(colRow, chosenBuilding, dispatch)}>
                    {occupied === true ?
                        <div>
                            <div style={{backgroundImage: `url(${imageURL})`}} className={styles.building_image}/>
                            {label}
                        </div> :
                        <div>Unoccupied slot</div>
                    }
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

function onCellClick(slot: string, chosenBuilding: any, dispatch: Dispatch) {
    dispatch(setSelectedPlanetSlot(slot, chosenBuilding));
    dispatch(clearSelectedOptionSlot());
}

const planetBuildings = {
    1: {
        occupied: false,
        excluded: ["iron_mine"],
        included: []
    },
    2: {
        occupied: true,
        label: "Iron mine",
        type: "iron_mine",
        imageURL: require("static/iron_mine.jpg"),
        excluded: ["ALL"],
        included: ["iron_mine"]
    },
    3: {
        occupied: true,
        label: "Iron mine",
        type: "iron_mine",
        imageURL: require("static/iron_mine.jpg"),
        excluded: ["ALL"],
        included: ["iron_mine"]
    }
};

const mapStateToProps = (state: StoreModel) =>
    ({selectedBuildingSlot: state.buildings.selectedBuildingSlot});

export default connect(mapStateToProps)(PlanetBuildingGrid);

