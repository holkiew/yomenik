import styles from "components/building/planetbuildinggrid.module.css";
import React, {useState} from 'react';
import {connect} from "react-redux";
import {Dispatch} from 'redux';
import {setSelectedPlanetSlot} from "./actions"

interface PlanetBuildingGridProps {
    dispatch: Dispatch
}

const PlanetBuildingGrid = (props: PlanetBuildingGridProps) => {
    const [state, setState] = useState("selectedCell");
    return (
        <div>
            <table>
                <tbody>
                {generateBuildingsGrid(props, state, setState)}
                </tbody>
            </table>
        </div>)
};

function generateBuildingsGrid(props: PlanetBuildingGridProps, selected: any, setState: any): any[] {
    const {dispatch} = props;
    const cols = 3;
    const rows = 1;
    const table = [];

    const createRow = (row: number): any[] => {
        const tds = [];
        for (let col = 0; col < cols; col++) {
            const chosenBuilding = planetBuildings[(row * cols) + col + 1];
            const {occupied, imageURL, label} = chosenBuilding;
            const slot = col + 1;

            tds.push(
                <td key={`${row}${col}`} className={`${styles.table_cell} ${selected === slot && styles.table_cell_selected}`} onClick={() => {
                    setState(slot);
                    dispatch(setSelectedPlanetSlot(slot, chosenBuilding))
                }}>
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

const planetBuildings = {
    1: {
        occupied: false,
        excluded: ["iron_mine"]
    },
    2: {
        occupied: true,
        label: "Iron mine",
        type: "iron_mine",
        imageURL: require("static/iron_mine.jpg"),
    },
    3: {
        occupied: true,
        label: "Iron mine",
        type: "iron_mine",
        imageURL: require("static/iron_mine.jpg"),
    }
};

export default connect()(PlanetBuildingGrid);

