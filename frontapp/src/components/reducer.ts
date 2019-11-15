import {Action, handleActions} from 'redux-actions';
import {SET_FOCUSED_PLANET, SET_PLANETS_DATA} from "./actions";
import {ComponentsState, DataState} from "components/ComponentsState";

const initialState: ComponentsState = {
    focusedPlanet: undefined,
    data: [],
    dataState: DataState.TO_UPDATE
};

export default handleActions<ComponentsState, any>({
    [SET_PLANETS_DATA]: (state: ComponentsState, action: Action<any>): ComponentsState => {
        const newState = {...state, data: action.payload.planets.slice(0)};
        if (!state.focusedPlanet && newState.data.length > 0) {
            newState.focusedPlanet = newState.data[0]
        }
        return newState;
    },
    [SET_FOCUSED_PLANET]: (state: ComponentsState, action: Action<string>): ComponentsState => {
        return {...state, focusedPlanet: state.data.find((planet: any) => planet.id === action.payload)};
    }
    // [MARK_DATA_TO_UPDATE]: (state: ComponentsState, action: Action<DataState>): ComponentsState => {
    //     return {...state, dataState: action.payload};
    // }

}, initialState);