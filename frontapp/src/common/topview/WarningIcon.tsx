import React, {useEffect, useState} from 'react';
import {IoIosWarning} from 'react-icons/io';
import {Popover, PopoverBody, PopoverHeader} from 'reactstrap';
import styles from "./warningicon.module.css"
import {MissionType} from "components/fleet/MissionType";

interface WarningCellProps {
    planetsData: [{
        onRouteFleets: { [missionType: string]: any },
        coordinates: { x: string, y: string },
        userId: string
    }]
}

enum WarningIconState {
    WARNING,
    INFO,
    NONE
}

interface WarningCellState {
    warningIconState: WarningIconState,
    popupVisible: boolean
}

const WarningIcon = (props: WarningCellProps) => {
    const initialState = {warningIconState: WarningIconState.NONE, popupVisible: false};
    const [state, setState] = useState<WarningCellState>(initialState);

    useEffect(() => componentDidUpdate(props, state, setState));

    return (
        <div>
            <IoIosWarning id="warning_icon" className={`${styles.icon}  ${getStyleForIconState(state)}`} onMouseEnter={() => setState({...state, popupVisible: true})}
                          onMouseLeave={() => setState({...state, popupVisible: false})}/>
            <Popover placement="bottom" isOpen={state.popupVisible} target="warning_icon" toggle={() => setState({...state, popupVisible: !state.popupVisible})}>
                <PopoverHeader className={styles.popover_header}>Ongoing missions</PopoverHeader>
                <PopoverBody className={styles.popover_body}>{fillPopupBody(props)}</PopoverBody>
            </Popover>
        </div>)
};

function fillPopupBody(props: WarningCellProps) {
    let counter = 0;
    return props.planetsData.map(planetData => {
        return Object.entries(planetData.onRouteFleets).map(([missionType, id]) => {
            counter++;
            return `${counter}. ${missionType}: ${id} \n`;
        });
    })
}

function componentDidUpdate(props: WarningCellProps, state: WarningCellState, setState: any) {
    const warningIconState = getWarningIconState(props);
    if (state.warningIconState !== warningIconState) {
        setState({...state, warningIconState});
    }
}

function getWarningIconState({planetsData}: WarningCellProps): WarningIconState {
    let warningIconState = WarningIconState.NONE;
    for (const planet of planetsData) {
        const onRouteFleetEntries = Object.entries(planet.onRouteFleets);
        if (onRouteFleetEntries.length !== 0) {
            warningIconState = WarningIconState.INFO;
            if (onRouteFleetEntries.find(([missionType]) => missionType === MissionType.ATTACK)) {
                warningIconState = WarningIconState.WARNING;
                break;
            }
        }
    }
    return warningIconState;
}

function getStyleForIconState({warningIconState}: WarningCellState): string {
    switch (warningIconState) {
        case WarningIconState.WARNING:
            return styles.icon_active;
        case WarningIconState.INFO:
            return "";
        case WarningIconState.NONE:
            return "";
    }
}

export default WarningIcon;

