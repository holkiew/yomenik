import BuildingReducer from "components/building/reducer";
import FleetReducer from "components/fleet/reducer";
import PlanetsDataReducer from "components/reducer";
import {combineReducers} from 'redux';

const rootReducer = combineReducers({
    planets: PlanetsDataReducer,
    fleets: FleetReducer,
    buildings: BuildingReducer
});

export default rootReducer;
