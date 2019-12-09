import LeftMenu from "common/leftmenu/LeftMenu";
import PlanetView from "common/rightplanetview/PlanetView";
import FleetPanel from "components/fleet/FleetPanel";
import Login from "components/login/Login";
import HomePanel from "components/panel/HomePanel";
import React from 'react';
import {BrowserRouter, Route, Switch} from "react-router-dom";
import {Col, Row} from 'reactstrap';
import SecureRoute from "security/SecureRoute";
import {isTokenStored} from "security/TokenUtil";
import GalaxyPanel from "./components/galaxy/GalaxyPanel";
import ResourceBar from "./common/topview/ResourceBar";

const Router = () =>
    <BrowserRouter>
        <div className="background-container">
            <Row>
                {isTokenStored() && <Col className="col-xs-3 col-sm-2 col-md-1 col-lg-1">
                    <LeftMenu/>
                </Col>}
                <Col className="col-xs-6 col-sm-8 col-md-10 col-lg-10">
                    <ResourceBar/>
                    <Switch>
                        <Route exact path="/" component={Login}/>
                        <Route path="/login" component={Login}/>
                        <SecureRoute path="/panel" component={HomePanel}/>
                        <SecureRoute path="/fleet" component={FleetPanel}/>
                        <SecureRoute path="/galaxy" component={GalaxyPanel}/>
                    </Switch>
                </Col>
                {isTokenStored() && <Col className="col-xs-3 col-sm-2 col-md-1 col-lg-1">
                    <PlanetView/>
                </Col>}
            </Row>
        </div>
    </BrowserRouter>;


export default Router;
