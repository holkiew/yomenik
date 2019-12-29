import BuildingState from "components/building/BuildingState";
import {Action, handleActions} from 'redux-actions';
import {SET_SELECTED_PLANET_SLOT} from "./actions";

const initialState: BuildingState = {
    selectedSlot: {
        slotKey: 0,
        rules: {
            excluded: []
        }
    }

};

export default handleActions<BuildingState, any>({
    [SET_SELECTED_PLANET_SLOT]: (state: BuildingState, action: Action<{ slotKey: number, chosenBuilding: any }>): BuildingState => {
        const {slotKey, chosenBuilding} = action.payload;
        const newState = {...state, selectedSlot: {slotKey, rules: {excluded: chosenBuilding?.excluded ?? []}}};
        return newState;
    }
}, initialState);