import {Epic, ofType} from "redux-observable";
import {map, mergeMap} from 'rxjs/operators';
import {SEND_FLEET_ON_MISSION} from "./actions";
import StoreModel from "StoreModel";
import Axios from "axios-observable";
import * as env from "config.json";
import {UPDATE_PLANETS_DATA} from "../actions";

const updateFleetEpics: Epic<any, any, StoreModel> = (actionsObservable, state) =>
    actionsObservable.pipe(
        ofType(SEND_FLEET_ON_MISSION),
        mergeMap(action => {
                const {planetIdTo, selectedFleet, missionType} = state.value.fleets;
                const planetIdFrom = state.value.planets.focusedPlanet.id;
                return Axios.post(
                    `${env.backendServer.baseUrl}${env.backendServer.services.fleetTravel}`,
                    {planetIdFrom, planetIdTo, fleet: selectedFleet, missionType}
                ).pipe(
                    map((response) => {
                        action.type = UPDATE_PLANETS_DATA;
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