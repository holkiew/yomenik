import * as React from 'react';
import {Link, Redirect} from "react-router-dom";
import {hasRole, isTokenStored, removeToken} from "../security/TokenUtil";
import ROLES from "../security/Roles";
import Nav from "reactstrap/lib/Nav";
import {NavItem} from 'reactstrap';

interface HeaderState {
    logout: boolean
}

export default class Header extends React.Component<{}, HeaderState> {
    constructor(props: {}) {
        super(props);
        this.state = { logout: false };
    }

    public componentDidMount() {
        if (this.state.logout) {
            this.setState({ logout: false });
        }
    }

    public render() {
        return (<Nav className="navbar navbar-expand-sm bg-dark navbar-dark">
                    { this.state.logout && <Redirect to="/"/> }
                    {/*Brand*/}
                    <Link className="navbar-brand" to="/">ECMA</Link>
                    {/*Links*/}
                    {isTokenStored() && <NavItemLink to="/" name="Homepage"/>}
                    {hasRole(ROLES.PARTNER_USER) && <NavItemLink to="/company" name="Company"/>}
                    {isTokenStored() && <NavItemLink to="/profile" name="My profile"/>}
                    {hasRole(ROLES.USER) && <NavItemLink to="/assignedReviews" name="My assigned reviews"/>}
                    {hasRole(ROLES.ADMIN) && <NavItemLink to="/users" name="Users"/>}
                    {hasRole(ROLES.ADMIN) && <NavItemLink to="/companies" name="Companies"/>}
                    {isTokenStored() && <NavItemLink to="/eproposal" name="Event Proposals"/>}
                    {isTokenStored() ? <NavItem><Link to="#" onClick={this.logout}>Logout</Link></NavItem> : <NavItemLink to="/login" name="Login"/>}
                </Nav>);
    }

    private logout = () => {
        removeToken();
        this.setState({ logout: true });
    }
}

export const NavItemLink = (props: { to: string, name: string }) => (
    <NavItem>
        <Link className="nav-link" to={props.to}>{props.name}</Link>
    </NavItem>
);
