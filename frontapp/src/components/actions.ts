import {action} from "typesafe-actions"

export const SET_PLANETS_DATA = 'SET_PLANETS_DATA';
export const GET_PLANETS_DATA_REQUEST = 'GET_PLANETS_DATA_REQUEST';
export const SET_AUXILIARY_DATA = 'SET_AUXILIARY_DATA';
export const GET_AUXILIARY_DATA_REQUEST = 'GET_AUXILIARY_DATA_REQUEST';
export const SET_FOCUSED_PLANET = 'SET_FOCUSED_PLANET';

export const updatePlanetsDataRequest = () => action(GET_PLANETS_DATA_REQUEST);

export const getAuxiliaryDataRequest = () => action(GET_AUXILIARY_DATA_REQUEST);

export const setFocusedPlanet = (focusedPlanetId: string) => action(SET_FOCUSED_PLANET, focusedPlanetId);