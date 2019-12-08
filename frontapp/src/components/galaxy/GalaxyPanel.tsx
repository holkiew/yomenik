import {setMissionPlanetCoords, setMissionPlanetTo, setMissionType} from "components/fleet/actions";
import {MissionType} from "components/fleet/MissionType";
import styles from "components/galaxy/galaxypanel.module.css";
import {History} from "history"
import React from 'react';
import {connect} from "react-redux";
import {useHistory} from "react-router-dom";
import {Col, Row} from "reactstrap";
import {Dispatch} from 'redux';
import * as planetPNG from "static/planet.png";
import StoreModel from "StoreModel";

interface GalaxyPanelProps {
    dispatch: Dispatch
    visiblePlanets: any[];
}

const GalaxyPanel = (props: GalaxyPanelProps) => {
    const history = useHistory();
    return (
        <div>
            <Row>
                <Col className="col-xs-12 col-sm-8 col-md-4 col-lg-3 offset-lg-3">
                    <table>
                        <tbody>
                        {generateGalaxyTableView(props, history)}
                        </tbody>
                    </table>
                </Col>
            </Row>
        </div>);
};

function generateGalaxyTableView(props: GalaxyPanelProps, history: History): any[] {
    const {visiblePlanets, dispatch} = props;
    const gridSize = 10;
    const table = [];
    const occupiedPlanetsCoordinates = getVisibleOccupiedPlanetsCoordinates(visiblePlanets);

    const createRow = (row: number): any[] => {
        const tds = [];
        for (let col = 0; col <= gridSize; col++) {
            const planetCoords = occupiedPlanetsCoordinates.find(coords => coords.x === row && coords.y === col);
            tds.push(
                <td key={`${row}${col}`} className={styles.table_cell}>
                    {row === 0 ? col : (col === 0 && row)}
                    {planetCoords && (
                        <div style={{backgroundImage: `url(${planetPNG})`}} className={styles.planet_image}>
                            <div className={styles.functions_popup}>
                                <div className={styles.functions_popup_missions}
                                     onClick={() => dispatchToNewMission(dispatch, MissionType.MOVE, planetCoords, history)}>Move
                                </div>
                                <div className={styles.functions_popup_missions}
                                     onClick={() => dispatchToNewMission(dispatch, MissionType.ATTACK, planetCoords, history)}>Attack
                                </div>
                                <div className={styles.functions_popup_missions}
                                     onClick={() => dispatchToNewMission(dispatch, MissionType.TRANSFER, planetCoords, history)}>Transport
                                </div>
                            </div>
                        </div>)}
                </td>);
        }
        return tds;
    };

    for (let row = 0; row <= gridSize; row++) {
        table.push(
            <tr key={row}>
                {createRow(row)}
            </tr>)
    }
    return table;
}

function getVisibleOccupiedPlanetsCoordinates(visiblePlanets: any[]): Array<{ x: number, y: number }> {
    return visiblePlanets.map(planetData => planetData.coordinates);
}

function dispatchToNewMission(dispatch: Dispatch, missionType: MissionType, planetCoords: { x: number, y: number }, history: History) {
    dispatch(setMissionType(missionType));
    dispatch(setMissionPlanetTo(planetCoords.y.toString()));
    dispatch(setMissionPlanetCoords(planetCoords.x.toString(), planetCoords.y.toString()));
    history.push("/fleet");
}

const mapStateToProps = (state: StoreModel) => ({
    visiblePlanets: state.planets.data
});

export default connect(mapStateToProps)(GalaxyPanel)

