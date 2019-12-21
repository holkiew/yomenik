import {action} from "typesafe-actions"

export const SET_SELECTED_PLANET_SLOT = 'SET_SELECTED_PLANET_BUILDING';

export const setSelectedPlanetSlot = (slotKey: number, rules: {}) =>
    action(SET_SELECTED_PLANET_SLOT, {slotKey, rules});
