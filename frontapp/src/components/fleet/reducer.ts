import {Action, handleActions} from 'redux-actions';
import {SET_MISSION_PLANET_TO, SET_MISSION_TYPE, SET_SELECTED_FLEET} from "./actions";
import FleetState from "./FleetState";
import {MissionType} from "./MissionType";

const initialState: FleetState = {
    selectedFleet: {},
    missionType: MissionType.NONE,
    planetIdTo: ''
};

export default handleActions<FleetState, any>({
    [SET_SELECTED_FLEET]: (state: FleetState, action: Action<{ templateName: string, amount: number }>): FleetState => {
        const newState = {...state};
        const {templateName, amount} = action.payload;
        if (amount === 0) {
            delete newState.selectedFleet[templateName];
        } else {
            newState.selectedFleet[templateName] = amount;
        }
        return newState;
    },
    [SET_MISSION_TYPE]: (state: FleetState, action: Action<MissionType>): FleetState => {
        return {...state, missionType: action.payload};
    },
    [SET_MISSION_PLANET_TO]: (state: FleetState, action: Action<string>): FleetState => {
        return {...state, planetIdTo: action.payload};
    }
}, initialState);