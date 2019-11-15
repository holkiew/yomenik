export interface ComponentsState {
    data: object[],
    focusedPlanet?: object,
    dataState: DataState
}

export enum DataState {
    TO_UPDATE,
    UP_TO_DATE
}