import * as React from 'react';
import {BrowserRouter, Route, Switch} from "react-router-dom";

import Login from "components/login/Login";
import HomePanel from "components/homepage/HomepagePanel";
import SecureRoute from "./security/SecureRoute";

const Routes = () =>
    <BrowserRouter>
        <div>
            <Switch>
                <Route exact path="/" component={Login}/>
                <Route path="/login" component={Login}/>
                <SecureRoute path="/panel" component={HomePanel}/>
            </Switch>
        </div>
    </BrowserRouter>;


export default Routes;