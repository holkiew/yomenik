import PlanetsDataState from "./components/PlanetsDataState";
import FleetState from "./components/fleet/FleetState";

export default interface StoreModel {
    planets: PlanetsDataState,
    fleets: FleetState
}