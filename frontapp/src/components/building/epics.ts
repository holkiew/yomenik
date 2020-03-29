import Axios from "axios-observable";
import {SET_PLANETS_DATA_RESPONSE} from "components/actions";
import {BUILD_NEW_BUILDING_REQUEST} from "components/building/actions";
import * as env from "config.json";
import {Epic, ofType} from "redux-observable";
import {map, mergeMap} from 'rxjs/operators';
import StoreModel from "StoreModel";

const newBuildingRequest: Epic<any, any, StoreModel> = (actionsObservable, state) =>
    actionsObservable.pipe(
        ofType(BUILD_NEW_BUILDING_REQUEST),
        mergeMap(action => {
                const {selectedBuildingSlot: {slotKey}} = state.value.buildings;
                const planetId = state.value.planets.focusedPlanet.id;
                const {type} = state.value.buildings.selectedBuildingOption;
                return Axios.put(
                    `${env.backendServer.baseUrl}${env.backendServer.services.planet}/building`,
                    {planetId, slot: slotKey, buildingType: type}
                ).pipe(
                    map((response) => {
                        action.type = SET_PLANETS_DATA_RESPONSE;
                        return action;
                    })
                )
            }
        )
    );

export default [
    newBuildingRequest
];