import BuildingState from "components/building/BuildingState";
import {Action, handleActions} from 'redux-actions';
import {CLEAR_SELECTED_OPTION_SLOT, SET_SELECTED_OPTION_SLOT, SET_SELECTED_PLANET_SLOT} from "./actions";

export const initialState: BuildingState = {
    selectedBuildingSlot: {
        slotKey: "99",
        rules: {
            excluded: [],
            included: []
        }
    },
    selectedBuildingOption: "99"
};

export default handleActions<BuildingState, any>({
    [SET_SELECTED_PLANET_SLOT]: (state: BuildingState, action: Action<{ slotKey: string, chosenBuilding: any }>): BuildingState => {
        const {slotKey, chosenBuilding} = action.payload;
        const newState = {...state, selectedBuildingSlot: {slotKey, rules: {...chosenBuilding}}};
        return newState;
    },
    [CLEAR_SELECTED_OPTION_SLOT]: (state: BuildingState): BuildingState => {
        const {selectedBuildingOption} = initialState;
        return {...state, selectedBuildingOption};
    },
    [SET_SELECTED_OPTION_SLOT]: (state: BuildingState, action: Action<{ selectedBuildingOption: string }>): BuildingState => {
        const {selectedBuildingOption} = action.payload;
        return {...state, selectedBuildingOption}
    }
}, initialState);