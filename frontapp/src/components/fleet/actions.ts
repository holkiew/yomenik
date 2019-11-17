import {action} from "typesafe-actions"
import {MissionType} from "./MissionType";

export const SET_SELECTED_FLEET = 'SET_SELECTED_FLEET';
export const SET_MISSION_TYPE = 'SET_MISSION_TYPE';
export const SET_MISSION_PLANET_TO = 'SET_MISSION_PLANET_TO';
export const SEND_FLEET_ON_MISSION = 'SEND_FLEET_ON_MISSION';
export const RESPONSE_SEND_FLEET_ON_MISSION = 'SEND_FLEET_ON_MISSION';

export const setSelectedFleet = (templateName: string, amount: number) =>
    action(SET_SELECTED_FLEET, {templateName, amount});

export const setMissionType = (missionType: MissionType) => action(SET_MISSION_TYPE, missionType);

export const setMissionPlanetTo = (planetIdTo: string) => action(SET_MISSION_PLANET_TO, planetIdTo);

export const sendFleetOnMission = () => action(SEND_FLEET_ON_MISSION);