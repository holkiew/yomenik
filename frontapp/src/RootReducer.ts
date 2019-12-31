import BuildingReducer from "components/building/reducer";
import PlanetEpics from "components/epics";
import FleetEpics from "components/fleet/epics";
import FleetReducer from "components/fleet/reducer";
import PlanetsDataReducer from "components/reducer";
import {combineReducers} from 'redux';
import {combineEpics} from "redux-observable";

export const rootEpic = combineEpics(
    PlanetEpics[0],
    FleetEpics[0],
);

export const rootReducer = combineReducers({
    planets: PlanetsDataReducer,
    fleets: FleetReducer,
    buildings: BuildingReducer
});
