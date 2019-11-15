import {Epic, ofType} from "redux-observable";
import {map, mergeMap} from 'rxjs/operators';
import {SET_PLANETS_DATA, UPDATE_PLANETS_DATA} from "./actions";
import StoreModel from "StoreModel";
import Axios from "axios-observable";
import * as env from "../config.json";
import {zip} from "rxjs";

const updatePlanetsEpic: Epic<any, any, StoreModel> = (actionsObservable, store) =>
    actionsObservable.pipe(
        ofType(UPDATE_PLANETS_DATA),
        mergeMap(action =>
            zip(
                Axios.get<any>(`${env.backendServer.baseUrl}${env.backendServer.services.planet}/planets`),
                Axios.get<any>(`${env.backendServer.baseUrl}${env.backendServer.services.fleetConfig}`))
                .pipe(
                    map(response => {
                        action.payload = {planets: response[0].data} as StoreModel;
                        action.type = SET_PLANETS_DATA;
                        return action;
                    })
                )
        )
    );

export default [
    updatePlanetsEpic
];