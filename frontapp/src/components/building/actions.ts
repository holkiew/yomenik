import {action} from "typesafe-actions"

export const SET_SELECTED_PLANET_SLOT = 'SET_SELECTED_PLANET_SLOT';
export const CLEAR_SELECTED_OPTION_SLOT = 'CLEAR_SELECTED_OPTION_SLOT';
export const SET_SELECTED_OPTION_SLOT = 'SET_SELECTED_OPTION_SLOT';

export const setSelectedPlanetSlot = (slotKey: string, chosenBuilding: {}) =>
    action(SET_SELECTED_PLANET_SLOT, {slotKey, chosenBuilding});

export const clearSelectedOptionSlot = () => action(CLEAR_SELECTED_OPTION_SLOT);

export const setSelectedOptionSlot = (selectedBuildingOption: string) => action(SET_SELECTED_OPTION_SLOT, {selectedBuildingOption});