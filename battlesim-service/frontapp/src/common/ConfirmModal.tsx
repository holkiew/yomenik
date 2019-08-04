import * as React from "react";
import {Button, Modal, ModalBody, ModalFooter, ModalHeader} from "reactstrap";

export interface ConfirmModalProps {
    openModal: boolean
    text: string
    confirmBtnText?: string
    cancelBtnText?: string

    toggle(): void

    callback(): void
}

export default class ConfirmModal extends React.Component<ConfirmModalProps> {
    public render() {
        return (<Modal isOpen={this.props.openModal} fade={false} toggle={this.props.toggle}>
            <ModalHeader toggle={this.props.toggle}>Confirm</ModalHeader>
            <ModalBody>{this.props.text}</ModalBody>
            <ModalFooter>
                <Button color="primary"
                        onClick={this.onConfirm}>{this.props.confirmBtnText !== undefined ? this.props.confirmBtnText : "Yes"}</Button>{' '}
                <Button color="secondary"
                        onClick={this.props.toggle}>{this.props.cancelBtnText !== undefined ? this.props.cancelBtnText : "Cancel"}</Button>
            </ModalFooter>
        </Modal>);
    }

    private onConfirm = () => {
        this.props.toggle();
        this.props.callback();
    }
};