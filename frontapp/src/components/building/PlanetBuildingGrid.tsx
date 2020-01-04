import styles from "components/building/planetbuildinggrid.module.css";
import {initialState as buildingInitialState} from "components/building/reducer";
import React from 'react';
import {connect} from "react-redux";
import {Dispatch} from 'redux';
import StoreModel from "StoreModel";
import {clearSelectedOptionSlot, setSelectedPlanetSlot} from "./actions"

interface PlanetBuildingGridProps {
    dispatch: Dispatch,
    focusedPlanet: {
        buildingSlots: number,
        buildings: any
    },
    selectedBuildingSlot: {
        slotKey: number
    }
}

const PlanetBuildingGrid = (props: PlanetBuildingGridProps) => {
    return (
        <div>
            <table>
                <tbody>
                {props.focusedPlanet && generateBuildingsGrid(props)}
                </tbody>
            </table>
        </div>)
};

function generateBuildingsGrid(props: PlanetBuildingGridProps): any[] {
    const {dispatch, selectedBuildingSlot: {slotKey}, focusedPlanet: {buildings, buildingSlots}} = props;
    const cols = 5;
    const rows = Math.ceil(buildingSlots / cols);
    const table = [];

    const createRow = (row: number): any[] => {
        const tds = [];
        const colsInRow = ((row + 1) * cols) > buildingSlots ? buildingSlots % cols : cols;
        for (let col = 0; col < colsInRow; col++) {
            const slot = (row * cols) + col + 1;
            const chosenBuilding = buildings[slot] ?? {...buildingInitialState.selectedBuildingSlot.specification, slot};
            const {type, label} = chosenBuilding;
            tds.push(
                <td key={slot} className={`${styles.table_cell} ${slotKey === slot && styles.table_cell_selected}`} onClick={() => onCellClick(slot, chosenBuilding, dispatch)}>
                    {chosenBuilding.type !== "unoccupied" ?
                        <div>
                            <div style={{backgroundImage: `url(${require(`static/${type}.jpg`)})`}} className={styles.building_image}/>
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

function onCellClick(slot: number, chosenBuilding: any, dispatch: Dispatch) {
    dispatch(setSelectedPlanetSlot(slot, chosenBuilding));
    dispatch(clearSelectedOptionSlot());
}

const mapStateToProps = (state: StoreModel) =>
    ({
        selectedBuildingSlot: state.buildings.selectedBuildingSlot,
        focusedPlanet: state.planets.focusedPlanet
    });

export default connect(mapStateToProps)(PlanetBuildingGrid);

