import * as React from 'react';
import {Collapse, ListGroupItem} from "reactstrap";
import {BattleHistoryRecapString} from "./HomepagePanel"

interface ListStageGroupItemProps {
    text: BattleHistoryRecapString
}

export default class ListStageGroupItem extends React.Component<ListStageGroupItemProps, any> {
    public readonly state = {
        isOpen: false
    };

    public render() {
        return (
            <div>
                <ListGroupItem action style={{cursor: "pointer"}}
                               onClick={() => this.setState({isOpen: !this.state.isOpen})}>{this.stageResolver(Number(this.props.text.stage))}</ListGroupItem>
                <Collapse isOpen={this.state.isOpen}>
                    {this.props.text.stageSummary}
                </Collapse>
            </div>
        );
    }

    private stageResolver(stage: number): string {
        if (stage === 0) {
            return "Initial fleet";
        } else if (stage === 999) {
            return "Final"
        } else {
            return `Stage ${stage}`;
        }
    }
}
