import React from 'react';
import {RouteComponentProps} from 'react-router-dom'
import * as env from "../../config.json";
import {fromEvent, Observable} from 'rxjs';
import {
    Button,
    Card,
    CardBody,
    CardHeader,
    CardText,
    Col,
    Container,
    Fade,
    FormFeedback,
    Input,
    InputGroup,
    Label,
    ListGroup,
    Row,
    Tooltip
} from 'reactstrap';
import Axios from "axios-observable";
// @ts-ignore
import {EventSourcePolyfill} from 'event-source-polyfill';
import {getRequestHeaderToken} from "security/TokenUtil";
import ShipForm from "./ShipForm";
import "./homepagepanel.css"
import CloseableListGroupItem from "./CloseableListGroupItem";
import BattleHistoryPageableList from "./BattleHistoryPageableList"

interface BattleHistoryDTO {
    battleRecapMap: {
        army1Recap: {},
        army2Recap: {}
    },
    startDate: Date,
    endDate: Date,
    isIssued: boolean
}

export interface BattleHistoryRecapString {
    stage: string,
    stageSummary: string
}

export default class HomepagePanel extends React.Component<RouteComponentProps, any> {
    public readonly state = {
        battleText: [] as BattleHistoryRecapString[],
        currentBattleVisible: false,
        stageIssueTime: 5,
        tooltipOpen: false
    };

    private shipForm1: React.RefObject<any> = React.createRef();
    private shipForm2: React.RefObject<any> = React.createRef();
    private stageIssueTimeRef: React.RefObject<any> = React.createRef();

    public componentDidMount(): void {
        this.stageIssueTimeRef.current.value = this.state.stageIssueTime;
        this.getCurrentBattleRequest()
            .subscribe(value => {
                const dto: BattleHistoryDTO = JSON.parse(value.data);
                const reacapStringList: BattleHistoryRecapString[] = this.battleHistoryToString(dto);
                this.setState({
                    battleText: reacapStringList,
                    currentBattleVisible: reacapStringList.length !== 0
                })
                }, error => {
                console.info(JSON.stringify(error))
                }
            );
    }

    // TODO refactor, lewa i prawa strona do oddzielnych komponentow
    public render() {
        return (
            <Container>
                <Col className="col-xs-12">
                    <Row>
                        <Col className="col-xs-12 col-sm-12 col-md-6 col-lg-6 text-center">
                            <Row className="justify-content-center">
                                <Col className="col-xs-12 col-sm-6 col-md-6 col-lg-5">
                                    <Label>Army 1</Label>
                                    <ShipForm ref={this.shipForm1}/>
                                </Col>
                                <Col className="col-xs-12 col-sm-6 col-md-6 col-lg-5 offset-lg-2">
                                    <Label>Army 2</Label>
                                    <ShipForm ref={this.shipForm2}/>
                                </Col>
                            </Row>
                            <Row style={{marginTop: "2rem"}}>
                                <Col className="col-xs-12 col-sm-12 col-md-12 col-lg-12 text-center">
                                    <InputGroup size="sm">
                                        <Label>Time between resolving following battle stages (sec):{"\u00a0"}</Label>
                                        <Input type="number" style={{maxWidth: "3rem"}}
                                               innerRef={this.stageIssueTimeRef} invalid={!this.isIssueTimeValid()}
                                               onChange={e => this.setState({stageIssueTime: e.target.value})}/>
                                        <FormFeedback>From 3 to 60 seconds</FormFeedback>
                                    </InputGroup>
                                </Col>
                            </Row>
                            <Row>
                                <Col className="col-xs-12 col-sm-12 col-md-12 col-lg-12 text-center">
                                    <Button onClick={this.postNewBattle} size="lg"
                                            style={{marginBottom: "1rem", marginTop: "1rem"}}>New
                                        battle</Button>
                                </Col>
                            </Row>
                            <Row>
                                <hr style={{borderTop: "1px solid #3c2c18", minWidth: "100%"}}/>
                            </Row>
                            <Row>
                                {this.state.currentBattleVisible &&
                                (<Fade in={this.state.currentBattleVisible} style={{minWidth: "100%"}}>
                                        <Card>
                                            <CardBody>
                                                <CardHeader>
                                                    {!this.state.battleText.find(value => value.stage === "999") &&
                                                    <Row className="justify-content-end">
                                                        <Button size="sm" onClick={this.cancelBattleRequest}
                                                                color="danger"
                                                                id="cancelButtonId"
                                                                style={{marginBottom: "1rem"}}>Cancel battle
                                                        </Button>
                                                        <Tooltip placement="top" isOpen={this.state.tooltipOpen}
                                                                 target="cancelButtonId"
                                                                 toggle={() => this.setState({tooltipOpen: !this.state.tooltipOpen})}
                                                        >Cancelling is penalized: Army 1 doesn't fire while retreating
                                                        </Tooltip>
                                                    </Row>}
                                                    <Row className="justify-content-center">Current battle</Row>
                                                </CardHeader>
                                                <CardText
                                                    style={{whiteSpace: "pre-wrap"}}>{this.listFromBattleHistoryString(this.state.battleText)}</CardText>
                                            </CardBody>
                                        </Card>
                                    </Fade>
                                )}
                            </Row>
                        </Col>
                        <Col className="col-md-offset-2 col-xs-12 col-sm-12 col-md-6 col-lg-6">
                            <Row className="justify-content-center">
                                <Col className="col-md-offset-2 col-xs-12 col-sm-12 col-md-10 col-lg-8 text-center">
                                    <BattleHistoryPageableList/>
                                </Col>
                            </Row>
                        </Col>
                    </Row>
                </Col>
            </Container>);
    }

    private battleHistoryToString(dto: BattleHistoryDTO): BattleHistoryRecapString[] {
        return Object.keys(dto.battleRecapMap).map(dtoKey => {
            let text = "";
            const armyRecap = dto.battleRecapMap[dtoKey];
            Object.keys(armyRecap).filter(v => v === "army1Recap" || v === "army2Recap").forEach(armyRecapKey => {
                text += `\tArmy: ${armyRecapKey}\n`;
                const armyShips = armyRecap[armyRecapKey];
                Object.keys(armyShips).forEach(destroyedOrLivingShipsMap => {
                    const shipsStateName = armyShips[destroyedOrLivingShipsMap];
                    if (Object.keys(shipsStateName).length !== 0) {
                        text += `\t\t${destroyedOrLivingShipsMap}:\n`;
                    }
                    Object.keys(shipsStateName).forEach(shipType => {
                        const count = shipsStateName[shipType];
                        text += `\t\t\t${shipType}: ${count}\n`;
                    })
                })
            });
            return {stage: dtoKey, stageSummary: text};
        });
    }

    private listFromBattleHistoryString = (stages: BattleHistoryRecapString[]): any => {
        let key = 0;
        return (
            <ListGroup>
                {stages.map(stage => <CloseableListGroupItem key={key += 1} collapsedText={stage.stageSummary}
                                                             text={this.stageNameResolver(Number(stage.stage))}/>)}
            </ListGroup>)
    };

    private postNewBattle = () => {
        if (this.isIssueTimeValid()) {
            const data = {
                army1: this.shipForm1.current.getData(),
                army2: this.shipForm2.current.getData(),
                stageDelay: this.state.stageIssueTime
            };
            console.info(data)
            Axios.post(`${env.backendServer.baseUrl}${env.backendServer.services.battlesim}/battle`,
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

    private stageNameResolver(stage: number): string {
        if (stage === 0) {
            return "Initial fleet";
        } else if (stage === 999) {
            return "Final"
        } else {
            return `Stage ${stage}`;
        }
    }
}




