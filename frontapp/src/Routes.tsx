import * as React from 'react';
import {BrowserRouter, Route, Switch} from "react-router-dom";

import Login from "components/login/Login";
import HomePanel from "components/homepage/HomepagePanel";
import SecureRoute from "./security/SecureRoute";
import {Col, Row} from 'reactstrap';
import LeftMenu from "./common/LeftMenu";

const Routes = () =>
    <BrowserRouter>
        <div className="background-container">
            <Row>
                <Col className="col-xs-3 col-sm-2 col-md-1 col-lg-1">
                    <LeftMenu/>
                </Col>
                <Col className="col-xs-9 col-sm-10 col-md-11 col-lg-11">
                    <Switch>
                        <Route exact path="/" component={Login}/>
                        <Route path="/login" component={Login}/>
                        <SecureRoute path="/panel" component={HomePanel}/>
                    </Switch>
                </Col>
            </Row>
        </div>
    </BrowserRouter>;


export default Routes;