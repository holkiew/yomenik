import React from 'react';
import Routes from "./Routes";
import {Dispatch} from "redux";
import StoreModel from "./StoreModel";
import {connect} from "react-redux";
import {DataState} from "components/ComponentsState";
import {updatePlanetsData} from "components/actions"
import {isTokenStored} from "./security/TokenUtil";


interface AppInterface {
    dataState: DataState,
    dispatch: Dispatch
}


class App extends React.Component<AppInterface> {
    readonly state = {};

    public static getDerivedStateFromProps(props: AppInterface) {
        if (props.dataState === DataState.TO_UPDATE && isTokenStored()) {
            props.dispatch(updatePlanetsData())
        }
        return null;
    }

    shouldComponentUpdate(nextProps: Readonly<AppInterface>, nextState: Readonly<any>, nextContext: any): boolean {
        console.warn("Root(APP) component render is locked");
        return false;
    }

    public render = () => <Routes/>;
}

const mapStateToProps = (store: StoreModel) => ({
    dataState: store.planets.dataState
});

export default connect(mapStateToProps)(App)

