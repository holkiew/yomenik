import * as React from 'react';
import {Redirect, Route} from "react-router-dom";
import {isTokenStored} from "./TokenUtil";

const SecureRoute = ({component: Component, ...rest}: any) => (
    <Route {...rest} render={(props) => (
        isTokenStored() ?
            <Component {...props}/> : <Redirect to='/login'/>
    )}/>
);

export default SecureRoute;
