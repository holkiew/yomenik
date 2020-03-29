import Axios from "axios-observable";
import FleetState from "components/fleet/FleetState";
import * as env from "config.json";
import {Action} from "redux-actions";
import {Epic, ofType} from "redux-observable";
import {map, mergeMap} from 'rxjs/operators';
import StoreModel from "StoreModel";
import {SET_PLANETS_DATA_RESPONSE} from "../actions";
import {ADD_CREATED_TEMPLATE_RESPONSE, GET_TEMPLATE_OPTIONS_REQUEST, SAVE_NEW_TEMPLATE_REQUEST, SEND_FLEET_ON_MISSION_REQUEST, SET_TEMPLATE_OPTIONS_RESPONSE} from "./actions";

const updateFleetEpics: Epic<Action<StoreModel>, Action<StoreModel>, StoreModel> = (actionsObservable, state) =>
    actionsObservable.pipe(
        ofType(SEND_FLEET_ON_MISSION_REQUEST),
        mergeMap(action => {
                const {planetIdTo, selectedFleet, missionType} = state.value.fleets;
                const planetIdFrom = state.value.planets.focusedPlanet.id;
                return Axios.post(
                    `${env.backendServer.baseUrl}${env.backendServer.services.fleetTravel}`,
                    {planetIdFrom, planetIdTo, fleet: selectedFleet, missionType}
                ).pipe(
                    map((response) => {
                        action.type = SET_PLANETS_DATA_RESPONSE;
                        action.payload = {
                            planets: response.data
                        } as StoreModel;
                        return action;
                    })
                )
            }
        )
    );

const getTemplateOptions: Epic<Action<FleetState>, Action<FleetState>, StoreModel> = (actionsObservable, state) =>
    actionsObservable.pipe(
        ofType(GET_TEMPLATE_OPTIONS_REQUEST),
        mergeMap(action => {
                return Axios.get(
                    `${env.backendServer.baseUrl}${env.backendServer.services.dataProvider}/template_options`
                ).pipe(
                    map((response) => {
                        action.type = SET_TEMPLATE_OPTIONS_RESPONSE;
                        action.payload = {
                            templateOptions: response.data.hullOptions
                        } as FleetState;
                        return action;
                    })
                )
            }
        )
    );

const newTemplateRequest: Epic<any, Action<FleetState>, StoreModel> = (actionsObservable, state) =>
    actionsObservable.pipe(
        ofType(SAVE_NEW_TEMPLATE_REQUEST),
        mergeMap(action => {
                return Axios.post(
                    `${env.backendServer.baseUrl}${env.backendServer.services.fleetManagement}/ship_template`,
                    action.payload
                ).pipe(
                    map((response) => {
                        action.type = ADD_CREATED_TEMPLATE_RESPONSE;
                        action.payload = {
                            templateOptions: response.data.shipGroupTemplates
                        } as FleetState;
                        return action;
                    })
                )
            }
        )
    );

export default [
    updateFleetEpics,
    getTemplateOptions,
    newTemplateRequest
];