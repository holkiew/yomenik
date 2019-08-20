import * as React from 'react';
import Routes from "./Routes";
import {configureAxios} from "./configuration";
import 'global.css'

export default class App extends React.Component {

    public componentWillMount() {
        configureAxios();
    }

    public render() {
        return (
                <Routes/>
        );
    }

}
