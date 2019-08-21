import * as React from 'react';
import Axios from "axios-observable";
import * as env from "../../config.json";
import ReactPaginate from "react-paginate";
import "./battlehistorypageablelist.css";
import {ListGroup} from "reactstrap";
import CloseableListGroupItem from "./CloseableListGroupItem";

interface BattleHistoryDTO {
    battleRecapMap: {
        army1Recap: {},
        army2Recap: {}
    },
    startDate: Date,
    endDate: Date,
    isIssued: boolean
}

export interface BattleHistoryDTORecapString {
    recap: BattleHistoryRecapString[],
    startDate: Date
}

export interface BattleHistoryRecapString {
    stage: string,
    stageSummary: string,
}

export default class BattleHistoryPageableList extends React.Component {
    public readonly state = {
        battleHistory: [] as BattleHistoryDTORecapString[],
        battleHistoryCount: 0
    };

    public render() {
        return (
            <div style={{width: "100%"}}>
                <p style={{fontSize: "large"}}> Battle history </p>
                <div>
                    <ReactPaginate
                        pageClassName="page-item"
                        nextLabel={<li className="page-item">></li>}
                        previousLabel={<li className="page-item">{"<"}</li>}
                        containerClassName="paginate"
                        activeLinkClassName="active-item"
                        activeClassName="active-item"
                        pageCount={Math.ceil(this.state.battleHistoryCount / 10)}
                        pageRangeDisplayed={3}
                        marginPagesDisplayed={1}
                        initialPage={0}
                        onPageChange={e => this.getBattleHistoryPageable(e.selected)}
                    />
                    {this.listFromBattleHistoryString(this.state.battleHistory)}
                </div>
            </div>
        );
    }

    private getBattleHistoryPageable = (page: number) => {
        Axios.get(`${env.backendServer.baseUrl}${env.backendServer.services.battlesim}/battleHistory`, {
            params: {
                page,
                size: 10,
                sort: "startDate,desc"
            }
        }).subscribe(value => {
            console.info(value)
            this.setState({
                battleHistory: this.battleHistoryToString(value.data.t1),
                battleHistoryCount: value.data.t2
            })
        });
    };

    private battleHistoryToString(dto: BattleHistoryDTO[]): BattleHistoryDTORecapString[] {
        return dto.map(dto => {
            const battleHistoryRecap = Object.keys(dto.battleRecapMap).map(dtoKey => {
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
                return {stage: dtoKey, stageSummary: text} as BattleHistoryRecapString;
            });
            return {startDate: dto.startDate, recap: battleHistoryRecap};
        })
    }

    private listFromBattleHistoryString = (battles: BattleHistoryDTORecapString[]): any => {
        let keyOut = 0;
        return (
            battles.map(battle => {
                let keyIn = 0;
                return (
                    <ListGroup key={keyOut += 1}>
                        <CloseableListGroupItem collapsedText={
                            <ListGroup>
                                {battle.recap.map(stage => <CloseableListGroupItem key={keyIn += 1}
                                                                                   collapsedText={<div
                                                                                       style={{whiteSpace: "pre-wrap"}}
                                                                                       className="list-group-item-action list-group-item">{stage.stageSummary}</div>}
                                                                                   text={this.stageNameResolver(Number(stage.stage))}/>)}
                            </ListGroup>
                        } text={new Date(battle.startDate).toLocaleString()}/>
                    </ListGroup>)

            }));
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
