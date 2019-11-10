import {Action, handleActions} from 'redux-actions';


export default handleActions<number, number>({
    ["ADD_NUMBER"]: (numer: number, akcja: Action<number>): number => {
        console.log(numer, akcja)
        return numer + 1;
    }
}, 0);