import React, {useEffect, useState} from 'react';
import {IoIosWarning} from 'react-icons/io';
import {Popover, PopoverBody, PopoverHeader} from 'reactstrap';
import styles from "./warningicon.module.css"

interface WarningCellProps {
    planetsData: [{
        onRouteFleets: { [missionType: string]: any },
        coordinates: { x: string, y: string },
        userId: string
    }]
}

interface WarningCellState {
    warningIconActive: boolean,
    popupVisible: boolean
}

const WarningIcon = (props: WarningCellProps) => {
    const initialState = {warningIconActive: false, popupVisible: false};
    const [state, setState] = useState<WarningCellState>(initialState);

    useEffect(() => componentDidUpdate(props, state, setState));

    return (
        <div>
            <IoIosWarning id="warning_icon" className={`${styles.icon}  ${(state.warningIconActive ? styles.icon_active : '')}`} onMouseEnter={() => setState({...state, popupVisible: true})}
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
    const warningIconActive = !!props.planetsData.find(planet => Object.keys(planet.onRouteFleets).length !== 0);
    if (state.warningIconActive !== warningIconActive) {
        setState({...state, warningIconActive});
    }
}

export default WarningIcon;

