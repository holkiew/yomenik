import BuildingState from "components/building/BuildingState";
import FleetState from "./components/fleet/FleetState";
import PlanetsDataState from "./components/PlanetsDataState";

export default interface StoreModel {
    planets: PlanetsDataState,
    fleets: FleetState,
    buildings: BuildingState
}