import {getTemplateOptionsRequest, saveNewTemplateRequest, setNewTemplateModal, toggleNewTemplateModal} from "components/fleet/actions";
import styles from "components/fleet/templatemodal.module.css";
import NewTemplateRequest from "model/components/fleet/request/NewTemplateRequest";
import * as React from "react";
import {ReactNode, useEffect, useState} from "react";
import {IoIosClose} from 'react-icons/io';
import {connect} from "react-redux";
import {Button, Col, Dropdown, DropdownItem, DropdownMenu, DropdownToggle, FormFeedback, Input, ListGroup, ListGroupItem, Modal, ModalBody, ModalFooter, ModalHeader, Row} from "reactstrap";
import {Dispatch} from "redux";
import StoreModel from "StoreModel";


interface ListTemplatesModal {
    isModalVisible: boolean
    templateOptions: {
        [key: string]: {
            weapons: []
        }
    }
    dispatch: Dispatch
}

const ListTemplatesModal = (props: ListTemplatesModal) => {
    const {isModalVisible, dispatch} = props;
    const [isWeaponDropdownVisible, setWeaponDropdownVisible] = useState(false);
    const [templateName, setTemplateName] = useState("New template");
    const [selectedWeapons, setSelectedWeapons] = useState(new Array<{ weaponName: string, key: string }>());
    const selectHullInitialValue: string = "Select hull";
    const [selectedHull, setSelectedHull] = useState(selectHullInitialValue);
    const isHullInitialValueChanged = () => selectedHull !== selectHullInitialValue;

    useEffect(() => {
        componentDidMount(props);
        return () => componentWillUnmount(props, setWeaponDropdownVisible)
    }, []);

    return (
        <div>
            <Modal isOpen={isModalVisible} toggle={() => dispatch(toggleNewTemplateModal())}>
                <ModalHeader className={styles.dropdown_title} toggle={() => dispatch(toggleNewTemplateModal())}>New template</ModalHeader>
                <ModalBody>
                    <Col>
                        <Row>
                            <Col size={5}>
                                <Input placeholder="Type template name" value={templateName}
                                       onChange={(e) => setTemplateName(e.target.value)}
                                       invalid={templateName.length < 3}
                                />
                            </Col>
                            <Col size={5}>
                                <FormFeedback>Min 3 characters</FormFeedback>
                                <Input type="select" name="select" defaultValue={selectedHull} className={styles.hullOptionsInput}
                                       onChange={e => setSelectedHull(e.target.value)}>
                                    <option value={selectedHull} disabled hidden>{selectedHull}</option>
                                    {getHullOptions(props)}
                                </Input>
                            </Col>
                        </Row>
                        <Row>
                            <div style={{height: "10px"}}/>
                        </Row>
                        <Row hidden={!isHullInitialValueChanged()}>
                            <Col size={6} className="text-center">
                                <Dropdown group isOpen={isWeaponDropdownVisible} size="sm"
                                          toggle={() => setWeaponDropdownVisible(!isWeaponDropdownVisible)}>
                                    <DropdownToggle>
                                        Add weapon
                                    </DropdownToggle>
                                    <DropdownMenu>
                                        {getAvailableWeaponsForSelectedHull(props, [selectedWeapons, setSelectedWeapons], selectedHull)}
                                    </DropdownMenu>
                                </Dropdown>
                            </Col>
                            <Col size={6}>
                                <p style={{color: "black"}} className="text-center">Selected weapons</p>
                                <div style={{overflowY: "auto", maxHeight: "10vh"}}>
                                    <ListGroup horizontal={true} className={`${styles.weapon_list_items}`}>
                                        {getSelectedWeaponList([selectedWeapons, setSelectedWeapons])}
                                    </ListGroup>
                                </div>
                            </Col>
                        </Row>
                    </Col>
                </ModalBody>
                <ModalFooter>
                    <Button color="success" onClick={() => dispatch(saveNewTemplateRequest(getNewTemplateRequest("SCATTER", selectedHull, templateName, selectedWeapons)))}>Save</Button>{' '}
                    <Button color="secondary" onClick={() => dispatch(toggleNewTemplateModal())}>Cancel</Button>
                </ModalFooter>
            </Modal>
        </div>
    );
};


function getAvailableWeaponsForSelectedHull(props: ListTemplatesModal, [includedWeapons, setIncludedWeapons]: [Array<{ weaponName: string, key: string }>, any], selectedHull: string): any[] {
    return props.templateOptions[selectedHull]?.weapons.map(weaponName => (<DropdownItem key={weaponName} onClick={() => {
        const key: string = weaponName + includedWeapons.length.toString();
        setIncludedWeapons([...includedWeapons, {weaponName, key}])
    }
    }>{weaponName}</DropdownItem>));
}

function getSelectedWeaponList([includedWeapons, setIncludedWeapons]: [Array<{ weaponName: string, key: string }>, any]): ReactNode {
    return includedWeapons.map((weaponObject) => (<ListGroupItem key={weaponObject.key}>
        <IoIosClose className={styles.list_close_icon} onClick={() => {
            setIncludedWeapons(includedWeapons.filter(includedWeaponObject => includedWeaponObject.key !== weaponObject.key))
        }}/>
        {weaponObject.weaponName}
    </ListGroupItem>));
}

function getHullOptions(props: ListTemplatesModal): ReactNode {
    return Object.keys(props.templateOptions).map(key => <option key={key} value={key}>{key}</option>)
}

function getNewTemplateRequest(fireMode: string, hullType: string, templateName: string, selectedWeapons: Array<{ weaponName: string, key: string }>): NewTemplateRequest {
    const weaponSlots = selectedWeapons
        .map((weaponObject, index) => ({[(index + 1)]: weaponObject.weaponName}))
        .reduce((obj, item) => ({...obj, ...item}), {});
    return {fireMode, hullType, templateName, weaponSlots};
}

function componentWillUnmount(props: ListTemplatesModal, isWeaponDropdownVisible: any) {
    const {dispatch} = props;
    dispatch(setNewTemplateModal(false));
    isWeaponDropdownVisible(false);
}

function componentDidMount(props: ListTemplatesModal) {
    const {dispatch} = props;
    dispatch(getTemplateOptionsRequest());
}

const mapStateToProps = (state: StoreModel) => {
    const {newTemplateModalVisible, templateOptions} = state.fleets;
    return {
        isModalVisible: newTemplateModalVisible,
        templateOptions
    };
};

export default connect(mapStateToProps)(ListTemplatesModal)
