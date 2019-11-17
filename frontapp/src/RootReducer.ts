import {combineReducers} from 'redux';
import exemplaryReducer from "reduxExampleTemplate/reducer";
import PlanetsDataReducer from "components/reducer";
import PlanetEpics from "components/epics";
import FleetEpics from "components/fleet/epics";
import {combineEpics} from "redux-observable";
import FleetReducer from "components/fleet/reducer";

export const rootEpic = combineEpics(
    PlanetEpics[0],
    FleetEpics[0],
);

export const rootReducer = combineReducers({
    exemplaryReducer,
    planets: PlanetsDataReducer,
    fleets: FleetReducer
});
