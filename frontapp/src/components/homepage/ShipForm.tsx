import * as React from 'react';
import {FormFeedback, Input, InputGroup, Label} from 'reactstrap';


export default class ShipForm extends React.Component<any, any> {
    public readonly state = {
        shipslvl1: undefined,
        shipslvl2: undefined,
        shipslvl3: undefined
    }

    public render() {
        return (
            <div>
                <InputGroup size="sm">
                    <Label>Ship level 1 {"\u00a0"}</Label>
                    <Input type="number"
                           invalid={!this.isInputValid(this.state.shipslvl1)}
                           onChange={(e) => this.setState({shipslvl1: e.target.value})}
                    />
                    <FormFeedback>max 1000</FormFeedback>
                </InputGroup>
                <InputGroup size="sm">
                    <Label>Ship level 2 {"\u00a0"}</Label>
                    <Input type="number"
                           invalid={!this.isInputValid(this.state.shipslvl2)}
                           onChange={(e) => this.setState({shipslvl2: e.target.value})}
                    />
                    <FormFeedback>max 1000</FormFeedback>
                </InputGroup>
                <InputGroup size="sm">
                    <Label>Ship level 3 {"\u00a0"}</Label>
                    <Input type="number"
                           invalid={!this.isInputValid(this.state.shipslvl3)}
                           onChange={(e) => this.setState({shipslvl3: e.target.value})}
                    />
                    <FormFeedback>max 1000</FormFeedback>
                </InputGroup>
            </div>
        );
    }

    public getData: {} = () => {
        return {
            shipsLevel1: this.state.shipslvl1,
            shipsLevel2: this.state.shipslvl2,
            shipsLevel3: this.state.shipslvl3
        }
    };

    private isInputValid = (value: any): boolean => {
        if (value === undefined) {
            return true
        } else {
            const val = Number(value);
            return val >= 0 && val <= 1000
        }
    }
}
