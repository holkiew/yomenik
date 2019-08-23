import * as React from 'react';
import * as env from "../../config.json";
import {Alert, Button, ButtonGroup, Container, Form, FormFeedback, FormGroup, Input} from 'reactstrap';
import {RouteComponentProps} from "react-router-dom";
import Axios from "axios-observable";
import {isTokenStored, removeToken, setToken} from "security/TokenUtil";
import logo from 'static/yomenik.png';
import "./login.css";

enum LOGIN_STATE {INITIAL, LOGIN_SUCCESSFUL, LOGIN_FAILED}

interface LoginState {
    loginState: LOGIN_STATE,
    errorReason: any,
    loginInput: string,
    passwordInput: string
    radioSelected: string
    formDataValidnessCheck: boolean
}

export default class Login extends React.Component<RouteComponentProps, LoginState> {
    public readonly state = {
        loginState: LOGIN_STATE.INITIAL,
        errorReason: "empty",
        loginInput: "",
        passwordInput: "",
        radioSelected: "login",
        formDataValidnessCheck: true
    };

    private loginButtonRef: React.RefObject<any> = React.createRef();

    public componentDidMount(): void {
        if (isTokenStored()) {
            this.props.history.push("/panel")
        }
    }

    public render() {
        return (
            <div className="black-canvas">
                <div className="flex-container">
                    <Container>
                        <Container className="col-xs-12 col-sm-12 col-lg-12">
                            <img src={logo} alt={logo} style={{width: "100%", marginBottom: "3rem"}}/>
                        </Container>
                        <Container className="col-xs-12 col-sm-8 col-lg-4" style={{textAlign: "center"}}>
                            <Form onSubmit={this.handleOnSubmitLogin}>
                                <FormGroup>
                                    <ButtonGroup style={{width: "100%"}}>
                                        <Button color="primary" style={{width: "50%"}}
                                                onClick={() => this.onRadioBtnClick("login")}
                                                active={this.state.radioSelected === "login"}>Sign in</Button>
                                        <Button color="primary" style={{width: "50%"}}
                                                onClick={() => this.onRadioBtnClick("register")}
                                                active={this.state.radioSelected === "register"}>Sign up</Button>
                                    </ButtonGroup>
                                </FormGroup>
                                <FormGroup>
                                    <Input type="text" name="login" id="login"
                                           invalid={!(this.state.formDataValidnessCheck || this.isInputValid(this.state.loginInput))}
                                           placeholder={this.state.radioSelected === "login" ? "login (default: user)" : "login"}
                                           onChange={(e) => this.setState({loginInput: e.target.value})}
                                           onKeyDown={e => e.key === 'Enter' ? this.loginOrRegister() : null}/>
                                    <FormFeedback>It has to be between 3 and 12 characters</FormFeedback>
                                </FormGroup>
                                <FormGroup>
                                    <Input type="password" name="password" id="password"
                                           invalid={!(this.state.formDataValidnessCheck || this.isInputValid(this.state.passwordInput))}
                                           placeholder={this.state.radioSelected === "login" ? "password (default: user)" : "password"}
                                           onChange={(e) => this.setState({passwordInput: e.target.value})}
                                           onKeyDown={e => e.key === 'Enter' ? this.loginOrRegister() : null}/>
                                    <FormFeedback>It has to be between 3 and 12 characters</FormFeedback>
                                </FormGroup>
                            </Form>
                            <Button onClick={this.loginOrRegister}
                                    innerRef={this.loginButtonRef}>{this.state.radioSelected === "login" ? "Login" : "Register"}</Button>
                        </Container>
                </Container>
                </div>
                <Container className="col-xs-12 col-sm-8 col-lg-4" style={{textAlign: "center"}}>
                    {this.renderAfterLoginMessage()}
                </Container>
            </div>

        );
    }

    private renderAfterLoginMessage = () => {
        switch (this.state.loginState) {
            case LOGIN_STATE.LOGIN_SUCCESSFUL:
                setTimeout(() => this.props.history.push("/panel"), 1500);
                return <Alert color="success"
                              style={{marginTop: "1rem"}}>{this.state.radioSelected === "login" ? "Login" : "Registration"} successful</Alert>;
            case LOGIN_STATE.LOGIN_FAILED:
                setTimeout(() => this.setState({loginState: LOGIN_STATE.INITIAL}), 5000);
                return (<Alert color="danger"
                               style={{marginTop: "1rem"}}>{this.state.radioSelected === "login" ? "Login" : "Registration"} failed {this.state.errorReason}</Alert>);
            default:
                return;
        }
    };

    private handleOnSubmitLogin = (event: any) => {
        event.preventDefault();
        this.loginOrRegister();
    };

    private onRadioBtnClick = (rSelected: string) => {
        this.setState({radioSelected: rSelected});
    };

    private isInputValid = (input: string): boolean => {
        return input.length >= 3 && input.length <= 12
        // && input.length !== 0
    };

    private loginOrRegister = () => {
        const areInputsValid = this.isInputValid(this.state.loginInput) && this.isInputValid(this.state.passwordInput);
        if (areInputsValid) {
            this.loginButtonRef.current.focus();
            const endpoint = this.state.radioSelected === "login" ? "/login" : "/register";
            Axios.post(`${env.backendServer.baseUrl}${env.backendServer.services.authentication}${endpoint}`, {
                username: this.state.loginInput,
                password: this.state.passwordInput
            }, {timeout: 3000})
                .toPromise()
                .then(res => {
                    setToken(res.data.token);
                    this.setState({
                        loginState: LOGIN_STATE.LOGIN_SUCCESSFUL
                    })
                })
                .catch(reason => {
                    removeToken();
                    this.loginButtonRef.current.blur();
                    this.setState({
                        loginState: LOGIN_STATE.LOGIN_FAILED,
                        errorReason: this.resolveErrorMessage(reason)
                    })
                })
        } else {
            this.setState({formDataValidnessCheck: false})
        }
    }

    private resolveErrorMessage = (reason: any): string => {
        let textReason;
        if (!reason.response) {
            textReason = "no server connection";
        } else {
            switch (reason.response.status) {
                case 400:
                    textReason = "username already taken";
                    break;
                case 401:
                    textReason = "wrong username or password";
                    break;
                case 500:
                    textReason = "internal error";
                    break;
                case 503:
                    textReason = "internal error";
                    break;
                default:
                    textReason = "";
            }
        }
        return textReason;
    }
}
