import {MissionType} from "components/fleet/MissionType";
import React, {useEffect, useState} from 'react';
import {IoIosWarning} from 'react-icons/io';
import {Popover, PopoverBody, PopoverHeader} from 'reactstrap';
import styles from "./warningicon.module.css"

interface WarningCellProps {
    planetsData: [{
        onRouteFleets: { [missionType: string]: any[] },
        coordinates: { x: string, y: string },
        userId: string
    }]
}

enum WarningIconStateEnum {
    WARNING,
    INFO,
    NONE
}

interface WarningCellState {
    warningIconState: WarningIconStateEnum,
    popupVisible: boolean
}

const WarningIcon = (props: WarningCellProps) => {
    const initialState = {warningIconState: WarningIconStateEnum.NONE, popupVisible: false};
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
    const filledIds = new Set();
    return props.planetsData.map(planetData => Object.entries(planetData.onRouteFleets)
        .map(([missionType, fleetList]) => fleetList
            .map(fleet => {
                const {id, ships, arrivalTime, planetIdFrom, planetIdTo} = fleet;
                if (!filledIds.has(id)) {
                    const arrivalTimeDate = new Date(arrivalTime);
                    const shipsSum = Object.values(ships).reduce((a, b) => Number(a) + Number(b), 0);

                    filledIds.add(id);
                    counter++;
                    return `${counter}. ${missionType}(${shipsSum}): From ${planetIdFrom} To ${planetIdTo} => ${arrivalTimeDate.toLocaleString()}\n`;
                }
                return;
            })
        )
    )
}

function componentDidUpdate(props: WarningCellProps, state: WarningCellState, setState: any) {
    const warningIconState = getWarningIconState(props);
    if (state.warningIconState !== warningIconState) {
        setState({...state, warningIconState});
    }
}

function getWarningIconState({planetsData}: WarningCellProps): WarningIconStateEnum {
    let warningIconState = WarningIconStateEnum.NONE;
    for (const planet of planetsData) {
        const onRouteFleetEntries = Object.entries(planet.onRouteFleets);
        if (onRouteFleetEntries.length !== 0) {
            warningIconState = WarningIconStateEnum.INFO;
            if (onRouteFleetEntries.find(([missionType]) => missionType === MissionType.ATTACK)) {
                warningIconState = WarningIconStateEnum.WARNING;
                break;
            }
        }
    }
    return warningIconState;
}

function getStyleForIconState({warningIconState}: WarningCellState): string {
    switch (warningIconState) {
        case WarningIconStateEnum.WARNING:
            return styles.icon_warning;
        case WarningIconStateEnum.INFO:
            return styles.icon_info;
        case WarningIconStateEnum.NONE:
            return "";
    }
}

export default WarningIcon;

