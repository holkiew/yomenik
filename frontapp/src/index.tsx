import 'bootstrap/dist/css/bootstrap.css';
import * as env from 'config.json';
import 'global.css'
import React from 'react';
import ReactDOM from 'react-dom';
import {Provider} from 'react-redux';
import {applyMiddleware, compose, createStore, Store} from 'redux';
import {createEpicMiddleware} from "redux-observable"
import rootEpic from "RootEpic";
import rootReducer from "RootReducer";
import StoreModel from "StoreModel";
import App from "./App";
import {configureAxios, initProdDebugUtils} from "./configuration";
import registerServiceWorker from './registerServiceWorker';

const initialState = {};

const composeEnhancers = ((window as any).__REDUX_DEVTOOLS_EXTENSION_COMPOSE__) || compose;
const epicMiddleware = createEpicMiddleware();
const store: Store<StoreModel> = createStore(rootReducer, initialState, composeEnhancers(applyMiddleware(epicMiddleware)));

// @ts-ignore
epicMiddleware.run(rootEpic);
registerServiceWorker();
configureAxios();
env.debug.value && initProdDebugUtils();

ReactDOM.render(
    <Provider store={store}>
        <div className="background-wallpaper">
            <App/>
        </div>
    </Provider>,
    document.getElementById('root') as HTMLElement
);


