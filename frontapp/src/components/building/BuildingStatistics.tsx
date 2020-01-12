import React from 'react';
import {connect} from "react-redux";
import {Dispatch} from 'redux';
import StoreModel from "StoreModel";

interface BuildingStatisticsProps {
    dispatch: Dispatch,
    selectedBuildingType: string
    configurationData: {
        id: string,
        creationDate: Date,
        configuration: { [buildingType: string]: [] }
    }
}

const BuildingStatistics = (props: BuildingStatisticsProps) => {
    const {selectedBuildingType} = props;
    return (
        <div>
            {selectedBuildingType.length > 0 && (
                <div>
                    <h2>{selectedBuildingType}</h2>
                    <table>
                        <tbody>
                        {generateBuildingStatisticsTableContent(props)}
                        </tbody>
                    </table>
                </div>)
            }
        </div>
    )
};


function generateBuildingStatisticsTableContent(props: BuildingStatisticsProps): any[] {
    const {selectedBuildingType, configurationData: {configuration}} = props;
    const stats: any[] = configuration[selectedBuildingType.toUpperCase()] ?? [];
    const table = [];

    table.push(
        <tr key={"first_row_stats"}>
            <th>Level</th>
            <th>Production/h</th>
            <th>Cost</th>
        </tr>);
    console.info(stats[1].cost)
    for (let row = 1; row <= Object.keys(stats).length; row++) {
        table.push(
            <tr key={row}>
                <td>{row}</td>
                <td>{stats[row].output.iron.amount}</td>
                <td>{stats[row].cost.iron.amount}</td>
            </tr>)
    }
    return table;
}

const mapStateToProps = (state: StoreModel) => ({
    selectedBuildingType: state.buildings.selectedBuildingOption.type,
    configurationData: state.planets.configurationData
});

export default connect(mapStateToProps)(BuildingStatistics)

