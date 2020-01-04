import Axios from "axios-observable";
import * as env from "config.json";
import {Epic, ofType} from "redux-observable";
import {map, mergeMap} from 'rxjs/operators';
import StoreModel from "StoreModel";
import {GET_PLANETS_DATA_REQUEST} from "../actions";
import {SEND_FLEET_ON_MISSION_REQUEST} from "./actions";

const updateFleetEpics: Epic<any, any, StoreModel> = (actionsObservable, state) =>
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
                        action.type = GET_PLANETS_DATA_REQUEST;
                        action.payload = {
                            planets: response.data
                        } as StoreModel;
                        console.info(action)
                        return action;
                    })
                )
            }
        )
    );

export default [
    updateFleetEpics
];