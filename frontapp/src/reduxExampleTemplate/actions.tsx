import {createAction} from "redux-actions"

export const addNumber = createAction<number, number>(
    "ADD_NUMBER",
    (num: number) => num);