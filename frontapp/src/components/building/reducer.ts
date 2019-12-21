import BuildingState from "components/building/BuildingState";
import {Action, handleActions} from 'redux-actions';
import {SET_SELECTED_PLANET_SLOT} from "./actions";

const initialState: BuildingState = {
    selectedSlot: {
        slotKey: 0,
        rules: {}
    }

};

export default handleActions<BuildingState, any>({
    [SET_SELECTED_PLANET_SLOT]: (state: BuildingState, action: Action<{ slotKey: number, rules: {} }>): BuildingState => {
        const {slotKey, rules} = action.payload;
        return {...state, selectedSlot: {slotKey, rules}};
    }
}, initialState);