import NewTemplateRequest from "model/components/fleet/request/NewTemplateRequest";
import {action} from "typesafe-actions"
import {MissionType} from "./MissionType";

export const SET_SELECTED_FLEET = 'SET_SELECTED_FLEET';
export const SET_MISSION_TYPE = 'SET_MISSION_TYPE';
export const SET_MISSION_PLANET_TO = 'SET_MISSION_PLANET_TO';
export const SET_MISSION_PLANET_COORDS = 'SET_MISSION_PLANET_COORDS';
export const SEND_FLEET_ON_MISSION_REQUEST = 'SEND_FLEET_ON_MISSION_REQUEST';
export const TOGGLE_NEW_TEMPLATE_MODAL = 'TOGGLE_NEW_TEMPLATE_MODAL';
export const SET_NEW_TEMPLATE_MODAL = 'SET_NEW_TEMPLATE_MODAL';
export const GET_TEMPLATE_OPTIONS_REQUEST = 'GET_TEMPLATE_OPTIONS_REQUEST';
export const SET_TEMPLATE_OPTIONS_RESPONSE = 'SET_TEMPLATE_OPTIONS_RESPONSE';
export const SAVE_NEW_TEMPLATE_REQUEST = 'SAVE_NEW_TEMPLATE_REQUEST';
export const SET_AVAILABLE_TEMPLATES_RESPONSE = 'SET_AVAILABLE_TEMPLATES_RESPONSE';
export const GET_AVAILABLE_TEMPLATES_REQUEST = 'GET_AVAILABLE_TEMPLATES_REQUEST';
export const TOGGLE_LIST_TEMPLATES_MODAL = 'TOGGLE_LIST_TEMPLATES_MODAL';
export const SET_LIST_TEMPLATES_MODAL = 'SET_LIST_TEMPLATES_MODAL';
export const DELETE_TEMPLATE_REQUEST = 'DELETE_TEMPLATE_REQUEST';


export const setSelectedFleet = (templateName: string, amount: number) =>
    action(SET_SELECTED_FLEET, {templateName, amount});

export const setMissionType = (missionType: MissionType) => action(SET_MISSION_TYPE, missionType);

export const setMissionPlanetTo = (planetIdTo: string) => action(SET_MISSION_PLANET_TO, planetIdTo);

export const setMissionPlanetCoords = (x: string, y: string) => action(SET_MISSION_PLANET_COORDS, {x, y});

export const sendFleetOnMission = () => action(SEND_FLEET_ON_MISSION_REQUEST);

export const toggleNewTemplateModal = () => action(TOGGLE_NEW_TEMPLATE_MODAL);

export const setNewTemplateModal = (state: boolean) => action(SET_NEW_TEMPLATE_MODAL, state);

export const getTemplateOptionsRequest = () => action(GET_TEMPLATE_OPTIONS_REQUEST);

export const saveNewTemplateRequest = (request: NewTemplateRequest) => action(SAVE_NEW_TEMPLATE_REQUEST, request);

export const toggleListTemplatesModal = () => action(TOGGLE_LIST_TEMPLATES_MODAL);

export const setListTemplatesModal = (state: boolean) => action(SET_LIST_TEMPLATES_MODAL, state);

export const deleteTemplateRequest = (templateName: string) => action(DELETE_TEMPLATE_REQUEST, templateName);

export const getAvailableTemplatesRequest = () => action(GET_AVAILABLE_TEMPLATES_REQUEST);