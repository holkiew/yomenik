import {createAction} from "redux-actions"

export const SET_PLANETS_DATA = 'SET_PLANETS_DATA';
export const SET_FOCUSED_PLANET = 'SET_FOCUSED_PLANET';


export const setPlanetsData = createAction<[], []>(
    SET_PLANETS_DATA,
    (planetsData) => {
        return planetsData;
    });

export const setFocusedPlanet = createAction<string, string>(
    SET_FOCUSED_PLANET,
    (focusedPlanetId) => {
        return focusedPlanetId;
    });