import {MissionType} from "./MissionType";

export default interface FleetState {
    selectedFleet: { [key: string]: number; }
    missionType: MissionType,
    planetIdTo: string
    planetCoordinates: { x: string, y: string }
}