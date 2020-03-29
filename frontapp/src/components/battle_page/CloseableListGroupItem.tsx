import React from 'react';
import {Collapse, ListGroupItem} from "reactstrap";

interface ListStageGroupItemProps {
    collapsedText: any
    text: any
}

export default class CloseableListGroupItem extends React.Component<ListStageGroupItemProps, any> {
    public readonly state = {
        isOpen: false
    };

    public render() {
        return (
            <div>
                <ListGroupItem action style={{cursor: "pointer"}}
                               onClick={() => this.setState({isOpen: !this.state.isOpen})}>{this.props.text}</ListGroupItem>
                <Collapse isOpen={this.state.isOpen}>
                    {this.props.collapsedText}
                </Collapse>
            </div>
        );
    }
}
