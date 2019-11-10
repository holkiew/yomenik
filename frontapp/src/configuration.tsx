import Axios from 'axios-observable';
import * as env from "config.json";
import {getRequestHeaderToken, setToken} from "./security/TokenUtil";
import * as JWT from 'jwt-decode';

export function configureAxios() {
    Axios.defaults.baseURL = env.backendServer.baseUrl;
    Axios.defaults.headers.common['Content-Type'] = 'application/x-www-form-urlencoded';
    updateAxiosHeaderToken();
}

export function updateAxiosHeaderToken() {
    Axios.defaults.headers.common.Authorization = getRequestHeaderToken();
}

export function initProdDebugUtils() {
    setToken(env.debug.token);
    // @ts-ignore
    Axios.defaults.headers.common["PRINCIPAL-ID"] = JWT(env.debug.token).id
}