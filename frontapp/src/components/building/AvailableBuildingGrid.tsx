import {buildNewBuildingRequest, clearSelectedOptionSlot, setSelectedOptionSlot} from "components/building/actions";
import styles from "components/building/availablebuildinggrid.module.css";
import {initialState as buildingsInitialState} from "components/building/reducer";
import React, {useState} from 'react';
import {connect} from "react-redux";
import {Button} from "reactstrap";
import {Dispatch} from 'redux';
import StoreModel from "StoreModel";

interface BuildingGridProps {
    dispatch: Dispatch,
    focusedPlanet: {
        availableBuildings: [{
            label: string,
            type: string
        }]
    }
    selectedBuildingSlot: {
        slotKey: number
        specification: {
            excluded: string[],
            included: string[]
        }
    },
    selectedBuildingOption: {
        slot: number
        type: string,
    }
}

const AvailableBuildingGrid = (props: BuildingGridProps) => {
    const [disabled, setDisabled] = useState(true);
    const {selectedBuildingSlot: {slotKey}, selectedBuildingOption: {slot}, dispatch} = props;
    const shouldBeDisabled = slotKey === buildingsInitialState.selectedBuildingSlot.slotKey || slot === buildingsInitialState.selectedBuildingOption.slot;
    if (shouldBeDisabled !== disabled) {
        setDisabled(!disabled);
    }
    return (
        <div>
            <table>
                <tbody>
                {props.focusedPlanet && generateBuildingsGrid(props)}
                </tbody>
            </table>
            <p style={{marginTop: "10vh"}}/>
            <Button color="success" disabled={disabled} onClick={() => dispatch(buildNewBuildingRequest())}>Build/Upgrade</Button>
        </div>)
};

function generateBuildingsGrid(props: BuildingGridProps): any[] {
    const {selectedBuildingOption, focusedPlanet: {availableBuildings}, dispatch} = props;
    const cols = 5;
    const rows = Math.ceil(availableBuildings.length / cols);
    const table = [];

    const createRow = (row: number): any[] => {
        const tds = [];
        const colsInRow = ((row + 1) * cols) > availableBuildings.length ? availableBuildings.length % cols : cols;
        for (let col = 0; col < colsInRow; col++) {
            const slot = (row * rows) + col;
            const {type, label} = availableBuildings[slot];
            const isExcludedAndGrayedOut = getIsExcludedAndGrayedOut(props, type);
            tds.push(
                <td key={slot} className={`${isExcludedAndGrayedOut ? styles.disabled_table_cell : styles.table_cell} ${selectedBuildingOption.slot === slot && styles.table_cell_selected}`}
                    onClick={() => onCellClick(isExcludedAndGrayedOut, {slot, type}, dispatch)}>
                    <div style={{backgroundImage: `url(${require(`static/${type}.jpg`)})`}} className={styles.building_image}/>
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

function getIsExcludedAndGrayedOut(props: BuildingGridProps, type: string): boolean {
    const {selectedBuildingSlot: {specification: {excluded, included}}} = props;
    if (excluded.length === 1 && excluded[0] === "ALL") {
        return !included.find(includedType => includedType === type)
    }
    return !!excluded.find(excludedType => excludedType === type);
}

function onCellClick(isExcludedAndGrayedOut: boolean, selectedBuildingOption: { slot: number, type: string }, dispatch: any) {
    if (!isExcludedAndGrayedOut) {
        dispatch(setSelectedOptionSlot(selectedBuildingOption));
    } else {
        dispatch(clearSelectedOptionSlot());
    }
}

const mapStateToProps = (state: StoreModel) => (
    {
        selectedBuildingSlot: state.buildings.selectedBuildingSlot,
        selectedBuildingOption: state.buildings.selectedBuildingOption,
        focusedPlanet: state.planets.focusedPlanet
    });

export default connect(mapStateToProps)(AvailableBuildingGrid)

