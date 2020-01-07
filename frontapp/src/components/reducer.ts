import PlanetsDataState from "components/PlanetsDataState";
import {Action, handleActions} from 'redux-actions';
import {SET_FOCUSED_PLANET, SET_PLANETS_DATA} from "./actions";

const initialState: PlanetsDataState = {
    focusedPlanet: undefined,
    fleetConfig: undefined,
    data: []
};

export default handleActions<PlanetsDataState, any>({
    [SET_PLANETS_DATA]: (state: PlanetsDataState, action: Action<any>): PlanetsDataState => {
        const newState = {
            ...state,
            data: action.payload.planets
                .map((p: any) => {
                    p.creationDate = new Date(p.creationDate);
                    return p
                })
                .sort((p1: any, p2: any) => p1.creationDate - p2.creationDate),
            fleetConfig: action.payload.fleetConfig
        };
        newState.focusedPlanet = !state.focusedPlanet ? newState.data[0] : newState.data.find((planet: any) => planet.id === state.focusedPlanet.id);
        return newState;
    },
    [SET_FOCUSED_PLANET]: (state: PlanetsDataState, action: Action<string>): PlanetsDataState => {
        return {...state, focusedPlanet: state.data.find((planet: any) => planet.id === action.payload)};
    }
}, initialState);
