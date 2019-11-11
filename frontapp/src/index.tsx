import React from 'react';
import ReactDOM from 'react-dom';
import {Provider} from 'react-redux';
import {createStore, Store} from 'redux';
import rootReducer from "./RootReducer";
import registerServiceWorker from './registerServiceWorker';
import 'bootstrap/dist/css/bootstrap.css';
import App from "./App";
import {configureAxios, initProdDebugUtils} from "./configuration";
import * as env from 'config.json';
import 'global.css'
import StoreModel from "./StoreModel";

const initialState = {};

const store: Store<StoreModel> = createStore(rootReducer, initialState, (window as any).__REDUX_DEVTOOLS_EXTENSION__ && (window as any).__REDUX_DEVTOOLS_EXTENSION__());

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

