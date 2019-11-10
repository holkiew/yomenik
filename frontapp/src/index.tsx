import * as React from 'react';
import * as ReactDOM from 'react-dom';
import {Provider} from 'react-redux';
import {createStore, Store} from 'redux';
import rootReducer from "./RootReducer";
import registerServiceWorker from './registerServiceWorker';
import 'bootstrap/dist/css/bootstrap.css';
import App from "./App";
import {configureAxios, initProdDebugUtils} from "./configuration";
import * as env from 'config.json';
import 'global.css'

const initialState = {};

const store: Store<any> = createStore(rootReducer, initialState);

registerServiceWorker();
configureAxios();


env.debug.value && initProdDebugUtils();

ReactDOM.render(
    <Provider store={store}>
        <App/>
    </Provider>,
    document.getElementById('root') as HTMLElement
);

