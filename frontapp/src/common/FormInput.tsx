import {InputType} from "reactstrap/lib/Input";
import * as React from "react";
import FormGroup from "reactstrap/lib/FormGroup";
import {FormFeedback, Input, Label} from "reactstrap";

export interface ValidateInputProps {
    id: string
    name?: string
    value?: string
    label?: string
    type: InputType
    placeholder?: string
    onchange(event: any): void
    getValidationMessage(id: string): string | undefined
}

export interface ValidationMessages {
    validationMessage: string
    errors: ValidationError[]
}

export interface ValidationError {
    field: string
    messages: string[]
}

export default class FormInput extends React.Component<ValidateInputProps> {
    public render() {
        return (
            <FormGroup>
                <Label for={this.props.id}>{this.props.label}</Label>
                <Input onChange={this.props.onchange}
                       type={this.props.type}
                       name={this.props.name}
                       defaultValue={this.props.value}
                       id={this.props.id}
                       placeholder={this.props.placeholder}
                       invalid={this.props.getValidationMessage(this.props.id) !== undefined} />
                { this.props.getValidationMessage(this.props.id) && <FormFeedback>{this.props.getValidationMessage(this.props.id)}</FormFeedback> }
            </FormGroup>
        );
    }
}
