import {action} from "typesafe-actions"

export const UPDATE_PLANETS_DATA = 'UPDATE_PLANETS_DATA';
export const SET_PLANETS_DATA = 'SET_PLANETS_DATA';
export const SET_FOCUSED_PLANET = 'SET_FOCUSED_PLANET';
export const MARK_DATA_TO_UPDATE = 'MARK_DATA_TO_UPDATE';

export const updatePlanetsData = () => action(UPDATE_PLANETS_DATA);

export const setFocusedPlanet = (focusedPlanetId: string) => action(SET_FOCUSED_PLANET, focusedPlanetId);