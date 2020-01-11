import Axios from "axios-observable";
import {Epic, ofType} from "redux-observable";
import {zip} from "rxjs";
import {map, mergeMap} from 'rxjs/operators';
import StoreModel from "StoreModel";
import * as env from "../config.json";
import {GET_AUXILIARY_DATA_REQUEST, GET_PLANETS_DATA_REQUEST, SET_AUXILIARY_DATA, SET_PLANETS_DATA} from "./actions";

const updatePlanets: Epic<any, any, StoreModel> = (actionsObservable, store) =>
    actionsObservable.pipe(
        ofType(GET_PLANETS_DATA_REQUEST),
        mergeMap(action =>
            zip(
                Axios.get<any>(`${env.backendServer.baseUrl}${env.backendServer.services.planet}/planets`),
                Axios.get<any>(`${env.backendServer.baseUrl}${env.backendServer.services.fleetConfig}`))
                .pipe(
                    map(response => {
                        action.payload = {
                            planets: response[0].data,
                            fleets: response[1].data
                        } as StoreModel;
                        action.type = SET_PLANETS_DATA;
                        return action;
                    })
                )
        )
    );

const getBuildingsConfiguration: Epic<any, any, StoreModel> = (actionsObservable, store) =>
    actionsObservable.pipe(
        ofType(GET_AUXILIARY_DATA_REQUEST),
        mergeMap(action =>
            Axios.get<any>(`${env.backendServer.baseUrl}${env.backendServer.services.planet}/configuration`)
                .pipe(
                    map(response => {
                        action.payload = {
                            dataConfiguration: response.data,
                        };
                        action.type = SET_AUXILIARY_DATA;
                        return action;
                    })
                )
        )
    );

export default [
    updatePlanets,
    getBuildingsConfiguration
];