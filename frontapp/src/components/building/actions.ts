import {action} from "typesafe-actions"

export const SET_SELECTED_PLANET_SLOT = 'SET_SELECTED_PLANET_SLOT';
export const CLEAR_SELECTED_OPTION_SLOT = 'CLEAR_SELECTED_OPTION_SLOT';
export const SET_SELECTED_OPTION_SLOT = 'SET_SELECTED_OPTION_SLOT';
export const BUILD_NEW_BUILDING_REQUEST = 'BUILD_NEW_BUILDING_REQUEST';

export const setSelectedPlanetSlot = (slotKey: number, chosenBuilding: {}) =>
    action(SET_SELECTED_PLANET_SLOT, {slotKey, chosenBuilding});

export const clearSelectedOptionSlot = () => action(CLEAR_SELECTED_OPTION_SLOT);

export const setSelectedOptionSlot = (selectedBuildingOption: { slot: number, type: string }) => action(SET_SELECTED_OPTION_SLOT, {selectedBuildingOption});

export const buildNewBuildingRequest = () => action(BUILD_NEW_BUILDING_REQUEST);
