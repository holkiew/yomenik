import Axios from  'axios-observable';
import * as env from "config.json";
import {getRequestHeaderToken} from "./security/TokenUtil";

export function configureAxios() {
    Axios.defaults.baseURL = env.backendServer.baseUrl;
    Axios.defaults.headers.common['Content-Type'] = 'application/x-www-form-urlencoded';
    updateAxiosHeaderToken();
}

export function updateAxiosHeaderToken() {
    Axios.defaults.headers.common.Authorization = getRequestHeaderToken();
}