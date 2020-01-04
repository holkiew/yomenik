import {action} from "typesafe-actions"
import {MissionType} from "./MissionType";

export const SET_SELECTED_FLEET = 'SET_SELECTED_FLEET';
export const SET_MISSION_TYPE = 'SET_MISSION_TYPE';
export const SET_MISSION_PLANET_TO = 'SET_MISSION_PLANET_TO';
export const SET_MISSION_PLANET_COORDS = 'SET_MISSION_PLANET_COORDS';
export const SEND_FLEET_ON_MISSION_REQUEST = 'SEND_FLEET_ON_MISSION_REQUEST';

export const setSelectedFleet = (templateName: string, amount: number) =>
    action(SET_SELECTED_FLEET, {templateName, amount});

export const setMissionType = (missionType: MissionType) => action(SET_MISSION_TYPE, missionType);

export const setMissionPlanetTo = (planetIdTo: string) => action(SET_MISSION_PLANET_TO, planetIdTo);

export const setMissionPlanetCoords = (x: string, y: string) => action(SET_MISSION_PLANET_COORDS, {x, y});

export const sendFleetOnMission = () => action(SEND_FLEET_ON_MISSION_REQUEST);