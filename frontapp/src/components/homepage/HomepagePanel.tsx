import * as React from 'react';
import {RouteComponentProps} from 'react-router-dom'
import * as env from "../../config.json";
import {fromEvent, Observable} from 'rxjs';
import {
    Button,
    Card,
    CardBody,
    CardHeader,
    CardText,
    CardTitle,
    Col,
    Container,
    Fade,
    FormFeedback,
    Input,
    InputGroup,
    Label,
    Row
} from 'reactstrap';
import Axios from "axios-observable";
// @ts-ignore
import {EventSourcePolyfill} from 'event-source-polyfill';
import {getRequestHeaderToken} from "security/TokenUtil";
import ShipForm from "./ShipForm";
import "./homepagepanel.css"

interface BattleHistoryDTO {
    battleRecapMap: {
        army1Recap: {},
        army2Recap: {}
    },
    startDate: Date,
    endDate: Date,
    isIssued: boolean,
    stageIssueTime: number
}

export default class HomepagePanel extends React.Component<RouteComponentProps, any> {
    public readonly state = {
        battleText: "",
        currentBattleVisible: false,
        stageIssueTime: 5
    };

    private shipForm1: React.RefObject<any> = React.createRef();
    private shipForm2: React.RefObject<any> = React.createRef();
    private stageIssueTimeRef: React.RefObject<any> = React.createRef();

    public componentDidMount(): void {
        this.stageIssueTimeRef.current.value = this.state.stageIssueTime;
        this.getCurrentBattleRequest()
            .subscribe(value => {
                const dto: BattleHistoryDTO = JSON.parse(value.data);
                console.info(dto)
                const text = this.battleHistoryToString(dto);
                this.setState({
                    battleText: text,
                    currentBattleVisible: text !== ""
                })
                }, error => {
                console.info(JSON.stringify(error))
                }
            );
    }

    public render() {
        return (

            <Container>
                <Row className="justify-content-md-center">
                    <Col className="col-xs-12 col-sm-6 col-md-4 col-lg-3" style={{border: "1px solid #d8d8d8"}}>
                        <Label>Shipy 1</Label>
                        <ShipForm ref={this.shipForm1}/>
                    </Col>
                    <Col className="col-xs-12 col-sm-6 col-md-4 col-lg-3 offset-lg-3"
                         style={{border: "1px solid #d8d8d8"}}>
                        <Label>Shipy 2</Label>
                        <ShipForm ref={this.shipForm2}/>
                    </Col>
                </Row>
                <Row className="justify-content-md-center">
                    <Button onClick={this.postNewBattle} style={{marginBottom: "1rem"}}>New battle</Button>
                </Row>
                <Row>
                    <InputGroup size="sm">
                        <Label>Stage issue delay (sec) {"\u00a0"}</Label>
                        <Input type="number" innerRef={this.stageIssueTimeRef} invalid={!this.isIssueTimeValid()}
                               onChange={e => this.setState({stageIssueTime: e.target.value})}/>
                        <FormFeedback>From 3 to 60 seconds</FormFeedback>
                    </InputGroup>
                </Row>
                <Row>
                    <Fade in={this.state.currentBattleVisible}>
                        <Card>
                            <CardBody>
                                <CardTitle>Current battle</CardTitle>
                                <CardHeader><Button onClick={this.cancelBattleRequest}
                                                    style={{marginBottom: "1rem"}}>cancel battle</Button></CardHeader>
                                <CardText style={{whiteSpace: "pre-wrap"}}>{this.state.battleText}</CardText>
                            </CardBody>
                        </Card>
                    </Fade>
                </Row>
            </Container>);
    }

    private battleHistoryToString(dto: BattleHistoryDTO) {
        let text: string = "";
        Object.keys(dto.battleRecapMap).forEach(dtoKey => {
            text += `STAGE ${dtoKey} RECAP\n`;
            const armyRecap = dto.battleRecapMap[dtoKey];
            Object.keys(armyRecap).filter(v => v === "army1Recap" || v === "army2Recap").forEach(armyRecapKey => {
                text += `\tArmy: ${armyRecapKey}\n`;
                const armyShips = armyRecap[armyRecapKey];
                Object.keys(armyShips).forEach(shipType => {
                    const count = armyShips[shipType];
                    text += `\t\t${shipType}: ${count}\n`;
                })
            })
        });
        return text;
    }

    private postNewBattle = () => {
        if (this.isIssueTimeValid()) {
            const data = {
                army1: this.shipForm1.current.getData(),
                army2: this.shipForm2.current.getData(),
                stageDelay: this.state.stageIssueTime
            };
            console.info(data)
            Axios.post(`${env.backendServer.baseUrl}${env.backendServer.services.battlesim}/newBattle`,
                data).subscribe(value => console.info(value));
        }
    };

    private isIssueTimeValid(): boolean {
        const stageDelayTime = this.state.stageIssueTime;
        return stageDelayTime >= 3 && stageDelayTime <= 60;

    }

    private cancelBattleRequest = () => {
        Axios.delete(`${env.backendServer.baseUrl}${env.backendServer.services.battlesim}/currentBattle`)
            .subscribe(value => console.info(value));
    };

    private getCurrentBattleRequest = (): Observable<MessageEvent> => {
        return new Observable<MessageEvent>(observer => {
            const source = new EventSourcePolyfill(`${env.backendServer.baseUrl}${env.backendServer.services.battlesim}/currentBattle`,
                {headers: {Authorization: getRequestHeaderToken()}});
            const message: Observable<MessageEvent> = fromEvent(source, 'message');
            const subscription = message.subscribe(observer);
            return () => {
                subscription.unsubscribe();
                source.close();
            };
        });
    };
}




