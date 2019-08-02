import * as React from 'react';
import {Input, InputGroup, Label} from 'reactstrap';


export default class ShipForm extends React.Component<any, any> {
    private shipslvl1: React.RefObject<any> = React.createRef();
    private shipslvl2: React.RefObject<any> = React.createRef();
    private shipslvl3: React.RefObject<any> = React.createRef();

    constructor(props: any) {
        super(props);
    }

    public render() {
        return (
            <div>
                <InputGroup size="sm">
                    <Label>Ship level 1 {"\u00a0"}</Label>
                    <Input type="number" innerRef={this.shipslvl1}
                        // onChange={(e) => this.setState({loginInput: e.target.value})}
                    />
                </InputGroup>
                <InputGroup size="sm">
                    <Label>Ship level 2 {"\u00a0"}</Label>
                    <Input type="number" innerRef={this.shipslvl2}
                        // onChange={(e) => this.setState({loginInput: e.target.value})}
                    />
                </InputGroup>
                <InputGroup size="sm">
                    <Label>Ship level 3 {"\u00a0"}</Label>
                    <Input type="number" innerRef={this.shipslvl3}
                        // onChange={(e) => this.setState({loginInput: e.target.value})}
                    />
                </InputGroup>
            </div>
        );
    }

    public getData: {} = () => {
        return {
            shipsLevel1: this.shipslvl1.current.value,
            shipsLevel2: this.shipslvl2.current.value,
            shipsLevel3: this.shipslvl3.current.value
        }
    }
}
