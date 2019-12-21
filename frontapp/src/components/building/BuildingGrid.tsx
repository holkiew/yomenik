import styles from "components/building/buildinggrid.module.css";
import React, {useState} from 'react';
import {connect} from "react-redux";
import {Dispatch} from 'redux';

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
    const availableBuildingsLength = Object.keys(availableBuildings).length;
    const maxCols = 5;
    const maxRows = Math.floor(availableBuildingsLength / maxCols + 1);
    const table = [];

    const createRow = (row: number): any[] => {
        const tds = [];
        const colsInRow = row + 1 >= maxRows ? availableBuildingsLength % maxCols : maxCols;
        for (let col = 0; col < colsInRow; col++) {
            const {type, label, imageURL} = availableBuildings[(row * maxRows) + col + 1];
            tds.push(
                <td key={`${row}${col}`} className={styles.table_cell} onClick={() => setState(type)}>
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

export default connect()(BuildingGrid)

