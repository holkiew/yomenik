import React, {useState} from 'react';
import {IoIosWarning} from 'react-icons/io';
import {Popover, PopoverBody, PopoverHeader} from 'reactstrap';
import styles from "./warningcell.module.css"

interface WarningCellProps {
    planetsData: [{
        onRouteFleets: any,
        coordinates: { x: string, y: string },
        userId: string
    }]
}

const WarningCell = (props: WarningCellProps) => {
    const isActive = !!props.planetsData.find(planet => !planet.onRouteFleets.empty);
    const [state, setState] = useState(false);
    return (
        <div>
            <IoIosWarning id="warning_icon" className={`${styles.icon}  ${(isActive ? styles.icon_active : '')}`} onMouseEnter={() => setState(true)} onMouseLeave={() => setState(false)}/>
            <Popover placement="bottom" isOpen={state} target="warning_icon" toggle={() => setState(!state)}>
                <PopoverHeader className={styles.popover_header}>Ongoing missions</PopoverHeader>
                <PopoverBody>1.[missionType]: [username] | [fleetAmount] [xF,yF] => [xT,yT], [arrivalTime]</PopoverBody>
            </Popover>
        </div>)
};

export default WarningCell;

