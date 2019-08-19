import * as React from 'react';
import {Link} from "react-router-dom";
import {isTokenStored, removeToken} from "../security/TokenUtil";
import Nav from "reactstrap/lib/Nav";
import {NavbarBrand, NavItem} from 'reactstrap';

// interface HeaderState {
// }

export default class Header extends React.Component<any, any> {
    // public readonly state = {
    // };

    public render() {
        if (isTokenStored()) {
            return (
                <Nav className="navbar navbar-expand-sm navbar-dark bg-dark">
                    <NavbarBrand>
                        <a href="https://github.com/holkiew">github.com/holkiew</a>
                    </NavbarBrand>
                    <Nav className="ml-auto" navbar>
                        <NavItem>
                            <Link onClick={removeToken} className="nav-link" to="/">Logout</Link>
                        </NavItem>
                    </Nav>
                </Nav>
            );
        } else {
            return <div/>;
        }

    }

}

const NavItemLink = (props: { to: string, name: string }) => (
    <NavItem>
        <Link className="nav-link" to={props.to}>{props.name}</Link>
    </NavItem>
);
