import * as React from 'react';
import * as env from "../../config.json";
import {Alert, Button, Container, Form, FormGroup, Input} from 'reactstrap';
import {RouteComponentProps} from "react-router-dom";
import Axios from "axios-observable";
import {isTokenStored, removeToken, setToken} from "security/TokenUtil";
import logo from '/static/yomenik.png';
import "./login.css";

enum LOGIN_STATE {INITIAL, LOGIN_SUCCESSFUL, LOGIN_FAILED}

interface LoginState {
    loginState: LOGIN_STATE,
    errorReason: any,
    loginInput: string,
    passwordInput: string
}

export default class Login extends React.Component<RouteComponentProps, LoginState> {
    public readonly state = {
        loginState: LOGIN_STATE.INITIAL,
        errorReason: "empty",
        loginInput: "",
        passwordInput: ""
    };

    public componentDidMount(): void {
        if (isTokenStored()) {
            this.props.history.push("/panel")
        }
    }

    public render() {
        return (
            <Container>
                <Container className="col-xs-12 col-sm-12 col-lg-12" style={{}}>
                    <img src={logo} alt={logo} style={{width: "100%", marginTop: "10%", marginBottom: "5%"}}/>
                </Container>
                <Container className="mt-3 col-xs-12 col-sm-6 col-lg-3" style={{textAlign: "center"}}>
                    <Form onSubmit={this.handleOnSubmitLogin}>
                        <FormGroup>
                            <Input type="text" name="login" id="login" placeholder="login (user)"
                                   onChange={(e) => this.setState({loginInput: e.target.value})}/>
                        </FormGroup>
                        <FormGroup>
                            <Input type="password" name="password" id="password" placeholder="password (user)"
                                   onChange={(e) => this.setState({passwordInput: e.target.value})}/>
                        </FormGroup>
                    </Form>
                    <Button onClick={this.login} style={{marginBottom: "1rem"}}>Login</Button>
                    {this.renderAfterLoginMessage()}
                </Container>
            </Container>
        );
    }

    private renderAfterLoginMessage = () => {
        switch (this.state.loginState) {
            case LOGIN_STATE.LOGIN_SUCCESSFUL:
                setTimeout(() => this.props.history.push("/panel"), 1500);
                return <Alert color="success">Login successful</Alert>;
            case LOGIN_STATE.LOGIN_FAILED:
                setTimeout(() => this.setState({loginState: LOGIN_STATE.INITIAL}), 3000);
                return (<Alert color="danger">Login failed {this.state.errorReason}</Alert>);
            default:
                return;
        }
    };

    private handleOnSubmitLogin = (event: any) => {
        event.preventDefault();
        this.login();
    };

    private login = () => {
        Axios.post(`${env.backendServer.baseUrl}${env.backendServer.services.authentication}/login`, {
            username: this.state.loginInput,
            password: this.state.passwordInput
        })
            .toPromise()
            .then(res => {
                setToken(res.data.token);
                this.setState({
                    loginState: LOGIN_STATE.LOGIN_SUCCESSFUL
                })
            })
            .catch(reason => {
                removeToken();
                console.info(reason)
                this.setState({
                    loginState: LOGIN_STATE.LOGIN_FAILED,
                    errorReason: ""
                })
            })
    }
}
