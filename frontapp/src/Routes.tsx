import React from 'react';
import {BrowserRouter, Route, Switch} from "react-router-dom";
import Login from "components/login/Login";
import HomePanel from "components/panel/HomePanel";
import FleetPanel from "components/fleet/FleetPanel";
import SecureRoute from "security/SecureRoute";
import {Col, Row} from 'reactstrap';
import LeftMenu from "common/leftmenu/LeftMenu";
import {isTokenStored} from "security/TokenUtil";
import PlanetView from "common/rightplanetview/PlanetView";

const Routes = () =>
    <BrowserRouter>
        <div className="background-container">
            <Row>
                {isTokenStored() && <Col className="col-xs-3 col-sm-2 col-md-1 col-lg-1">
                    <LeftMenu/>
                </Col>}
                <Col className="col-xs-6 col-sm-8 col-md-10 col-lg-10">
                    <Switch>
                        <Route exact path="/" component={Login}/>
                        <Route path="/login" component={Login}/>
                        <SecureRoute path="/panel" component={HomePanel}/>
                        <SecureRoute path="/fleet" component={FleetPanel}/>
                    </Switch>
                </Col>
                {isTokenStored() && <Col className="col-xs-3 col-sm-2 col-md-1 col-lg-1">
                    <PlanetView/>
                </Col>}
            </Row>
        </div>
    </BrowserRouter>;


export default Routes;
