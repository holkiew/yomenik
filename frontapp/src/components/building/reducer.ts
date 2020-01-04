import BuildingState from "components/building/BuildingState";
import {Action, handleActions} from 'redux-actions';
import {CLEAR_SELECTED_OPTION_SLOT, SET_SELECTED_OPTION_SLOT, SET_SELECTED_PLANET_SLOT} from "./actions";

export const initialState: BuildingState = {
    selectedBuildingSlot: {
        slotKey: 99,
        specification: {
            level: 0,
            slot: 0,
            type: "unoccupied",
            label: "unoccupied",
            included: [],
            excluded: []
        }
    },
    selectedBuildingOption: {
        slot: 99,
        type: ""
    }
};

export default handleActions<BuildingState, any>({
    [SET_SELECTED_PLANET_SLOT]: (state: BuildingState, action: Action<{ slotKey: number, chosenBuilding: any }>): BuildingState => {
        const {slotKey, chosenBuilding} = action.payload;
        const newState = {...state, selectedBuildingSlot: {slotKey, specification: {...chosenBuilding}}};
        return newState;
    },
    [CLEAR_SELECTED_OPTION_SLOT]: (state: BuildingState): BuildingState => {
        const {selectedBuildingOption} = initialState;
        return {...state, selectedBuildingOption};
    },
    [SET_SELECTED_OPTION_SLOT]: (state: BuildingState, action: Action<{ selectedBuildingOption: { slot: number, type: string } }>): BuildingState => {
        const {selectedBuildingOption} = action.payload;
        return {...state, selectedBuildingOption}
    }
}, initialState);