import {getAuxiliaryDataRequest, updatePlanetsDataRequest} from "components/actions"
import React from 'react';
import {connect} from "react-redux";
import {Dispatch} from "redux";
import Routes from "Routes";
import {isTokenStored} from "security/TokenUtil";
import StoreModel from "./StoreModel";


interface AppInterface {
    dispatch: Dispatch
}


class App extends React.Component<AppInterface> {
    readonly state = {};

    static getDerivedStateFromProps(props: AppInterface) {
        if (isTokenStored()) {
            props.dispatch(updatePlanetsDataRequest())
            props.dispatch(getAuxiliaryDataRequest())
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
});

export default connect(mapStateToProps)(App)

