import {clearSelectedOptionSlot, setSelectedOptionSlot} from "components/building/actions";
import styles from "components/building/optionbuildinggrid.module.css";
import {initialState as buildingsInitialState} from "components/building/reducer";
import React, {useState} from 'react';
import {connect} from "react-redux";
import {Button} from "reactstrap";
import {Dispatch} from 'redux';
import StoreModel from "StoreModel";

interface BuildingGridProps {
    dispatch: Dispatch,
    selectedBuildingSlot: {
        slotKey: string
        rules: {
            excluded: string[],
            included: string[]
        }
    },
    selectedBuildingOption: string
}

const OptionBuildingGrid = (props: BuildingGridProps) => {
    const [disabled, setDisabled] = useState(true);
    const {selectedBuildingSlot: {slotKey}, selectedBuildingOption} = props;
    const shouldBeDisabled = slotKey === buildingsInitialState.selectedBuildingSlot.slotKey || selectedBuildingOption === buildingsInitialState.selectedBuildingOption;
    if (shouldBeDisabled !== disabled) {
        setDisabled(!disabled);
    }
    return (
        <div>
            <table>
                <tbody>
                {generateBuildingsGrid(props)}
                </tbody>
            </table>
            <Button color="success" disabled={disabled}>Build/Upgrade</Button>
        </div>)
};

function generateBuildingsGrid(props: BuildingGridProps): any[] {
    const {selectedBuildingOption, dispatch} = props;
    const availableBuildingsLength = Object.keys(availableBuildings).length;
    const maxCols = 5;
    const maxRows = Math.floor(availableBuildingsLength / maxCols + 1);
    const table = [];

    const createRow = (row: number): any[] => {
        const tds = [];
        const colsInRow = row + 1 >= maxRows ? availableBuildingsLength % maxCols : maxCols;
        for (let col = 0; col < colsInRow; col++) {
            const {type, label, imageURL} = availableBuildings[(row * maxRows) + col + 1];
            const isExcludedAndGrayedOut = getIsExcludedAndGrayedOut(props, type);
            const colRow = `${row}${col}`;
            tds.push(
                <td key={colRow} className={`${isExcludedAndGrayedOut ? styles.disabled_table_cell : styles.table_cell} ${selectedBuildingOption === colRow && styles.table_cell_selected}`}
                    onClick={() => onCellClick(isExcludedAndGrayedOut, colRow, dispatch)}>
                    <div style={{backgroundImage: `url(${imageURL})`}} className={styles.building_image}/>
                    {label}
                </td>);
        }
        return tds;
    };

    for (let row = 0; row < maxRows; row++) {
        table.push(
            <tr key={row}>
                {createRow(row)}
            </tr>)
    }
    return table;
}

function getIsExcludedAndGrayedOut(props: BuildingGridProps, type: string): boolean {
    const {selectedBuildingSlot: {rules: {excluded, included}}} = props;
    if (excluded.length === 1 && excluded[0] === "ALL") {
        return !included.find(includedType => includedType === type)
    }
    return !!excluded.find(excludedType => excludedType === type);
}

function onCellClick(isExcludedAndGrayedOut: boolean, colRow: string, dispatch: any) {
    if (!isExcludedAndGrayedOut) {
        dispatch(setSelectedOptionSlot(colRow));
    } else {
        dispatch(clearSelectedOptionSlot());
    }
}

const availableBuildings = {
    1: {
        imageURL: require("static/iron_mine.jpg"),
        label: "Iron mine",
        type: "iron_mine"
    },
    2: {
        imageURL: require("static/concrete_factory.jpg"),
        label: "Concrete factory",
        type: "concrete_factory"
    },
    3: {
        imageURL: require("static/crystal_mines.png"),
        label: "Crystal mine",
        type: "crystal_mine"
    }
};

const mapStateToProps = (state: StoreModel) => (
    {
        selectedBuildingSlot: state.buildings.selectedBuildingSlot,
        selectedBuildingOption: state.buildings.selectedBuildingOption
    });

export default connect(mapStateToProps)(OptionBuildingGrid)

