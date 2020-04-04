import {deleteTemplateRequest, getTemplateOptionsRequest, setListTemplatesModal, toggleListTemplatesModal} from "components/fleet/actions";
import styles from "components/fleet/listtemplatesmodal.module.css";
import * as React from "react";
import {ReactNode, useEffect, useState} from "react";
import {FaTrashAlt} from 'react-icons/fa';
import JSONPretty from "react-json-pretty";
import {connect} from "react-redux";
import {Button, Card, CardBody, Col, Collapse, ListGroup, ListGroupItem, Modal, ModalBody, ModalFooter, ModalHeader, Row} from "reactstrap";
import {Dispatch} from "redux";
import StoreModel from "StoreModel";

interface ListTemplatesModalProps {
    isModalVisible: boolean
    availableTemplates: {
        [key: string]: {}
    },
    usedTemplates: Set<string>
    dispatch: Dispatch
}

const ListTemplatesModal = (props: ListTemplatesModalProps) => {
    const {isModalVisible, dispatch} = props;
    const [isOpenTemplatePropertiesState, setIsOpenTemplatePropertiesState] = useState({} as { [templateName: string]: boolean });
    useEffect(() => {
        componentDidMount(props);
        return () => componentWillUnmount(props);
    }, []);

    return (
        <div>
            <Modal isOpen={isModalVisible} toggle={() => dispatch(toggleListTemplatesModal())}>
                <ModalHeader className={styles.dropdown_title} toggle={() => dispatch(toggleListTemplatesModal())}>Existing templates</ModalHeader>
                <ModalBody>
                    <ListGroup>
                        {getAllTemplates(props, [isOpenTemplatePropertiesState, setIsOpenTemplatePropertiesState])}
                    </ListGroup>
                </ModalBody>
                <ModalFooter>
                    <Button color="secondary" onClick={() => dispatch(toggleListTemplatesModal())}>Exit</Button>
                </ModalFooter>
            </Modal>
        </div>
    );
};

function getAllTemplates(props: ListTemplatesModalProps, [isOpenState, setStateIsOpen]: any): ReactNode {
    const {availableTemplates, usedTemplates} = props;
    return Object.entries(availableTemplates).map(([templateName, templateProperties]) => {
        const toggleIsOpen = () => setStateIsOpen({...isOpenState, [templateName]: !isOpenState[templateName] ?? true});
        const isOpen = (): boolean => isOpenState[templateName] ?? false;
        const canBeDeleted = (): boolean => !usedTemplates.has(templateName);
        return (
            <ListGroupItem key={templateName} className={styles.list_group_template_item}>
                <Row>
                    <Col size={6}>
                        {templateName}
                    </Col>
                    <Col xs={{size: 5}}>
                        <Button color="info" size="sm" onClick={toggleIsOpen} className={"float-right"}>Show properties</Button>
                    </Col>
                    <FaTrashAlt className={`${styles.list_delete_template} ${canBeDeleted() ? styles.list_delete_template_red : ""}`} onClick={() => canBeDeleted() && props.dispatch(deleteTemplateRequest(templateName))}/>
                    <Collapse isOpen={isOpen()}>
                        <Card>
                            <CardBody className={styles.card_body_template_properties}>
                                <JSONPretty data={JSON.stringify(templateProperties)}/>
                            </CardBody>
                        </Card>
                    </Collapse>
                </Row>
            </ListGroupItem>
        )
    })
}

function componentWillUnmount(props: ListTemplatesModalProps) {
    const {dispatch} = props;
    dispatch(setListTemplatesModal(false));
}

function componentDidMount(props: ListTemplatesModalProps) {
    const {dispatch} = props;
    dispatch(getTemplateOptionsRequest());
}

const mapStateToProps = (state: StoreModel) => {
    const {listTemplatesModalVisible, availableTemplates} = state.fleets;
    const usedTemplates = new Set<string>();
    Object.entries(state.planets.data).forEach(([idx, data]) => {
        Object.keys(data.residingFleet).forEach(residingFleetTemplate => usedTemplates.add(residingFleetTemplate));
        // it ought to be driven from backend, or contain as well onRouteFleets
    });
    return {
        isModalVisible: listTemplatesModalVisible,
        availableTemplates,
        usedTemplates
    };
};

export default connect(mapStateToProps)(ListTemplatesModal)
