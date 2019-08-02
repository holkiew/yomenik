import * as JWT from 'jwt-decode';
import * as env from "../config.json";
import ROLES from "./Roles";
import {updateAxiosHeaderToken} from "configuration";

const EMPTY_TOKEN: string = "";

export interface TokenDTO {
    exp: number
    iat: number
    roles: []
    sub: string // userID
}

export interface RoleDTO {
    authority: string
}

export function setToken(tokenValue: string) {
    localStorage.setItem(env.security.localStorage.token, tokenValue);
    updateAxiosHeaderToken();
}

export function removeToken() {
    localStorage.setItem(env.security.localStorage.token, EMPTY_TOKEN);
    updateAxiosHeaderToken();
}

export function getToken(): string {
    const token: string | null = localStorage.getItem(env.security.localStorage.token);
    return token == null ? EMPTY_TOKEN : token;
}

export function getRequestHeaderToken(): string | null {
    const token = getToken();
    return token !== EMPTY_TOKEN ? `Bearer ${token}` : null;
}

export function isTokenStored(): boolean {
    let isProperTokenStored: boolean;
    try {
        isProperTokenStored = JWT(getToken()) != null
    } catch (e) {
        isProperTokenStored = false;
    }
    return isProperTokenStored;
}

export function decodedToken(): TokenDTO | null {
    const token: string | null = getToken();
    return JWT(token);
}

export function hasRole(role: ROLES): boolean {
    const tokenDTO: TokenDTO | null = decodedToken();
    if (tokenDTO) {
        return tokenDTO.roles.find((value: RoleDTO) => value.authority === role) !== undefined;
    } else {
        return false;
    }
}