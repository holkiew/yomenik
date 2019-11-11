import {Action, handleActions} from 'redux-actions';
import {SET_FOCUSED_PLANET, SET_PLANETS_DATA} from "./actions";
import ComponentsState from "components/ComponentsState";

const initialState: ComponentsState = {
    focusedPlanet: undefined,
    data: []
};

export default handleActions<ComponentsState, any>({
    [SET_PLANETS_DATA]: (state: ComponentsState, action: Action<[any]>): ComponentsState => {
        const newState = {...state, data: action.payload};
        if (!state.focusedPlanet && action.payload.length > 0) {
            newState.focusedPlanet = action.payload[0]
        }
        return newState;
    },
    [SET_FOCUSED_PLANET]: (state: ComponentsState, action: Action<string>): ComponentsState => {
        return {...state, focusedPlanet: state.data.find((planet: any) => planet.id === action.payload)};
    }
}, initialState);