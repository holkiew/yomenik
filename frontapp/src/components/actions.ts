import {action} from "typesafe-actions"

export const GET_PLANETS_DATA_REQUEST = 'GET_PLANETS_DATA_REQUEST';
export const SET_PLANETS_DATA = 'SET_PLANETS_DATA';
export const SET_FOCUSED_PLANET = 'SET_FOCUSED_PLANET';

export const updatePlanetsDataRequest = () => action(GET_PLANETS_DATA_REQUEST);

export const setFocusedPlanet = (focusedPlanetId: string) => action(SET_FOCUSED_PLANET, focusedPlanetId);