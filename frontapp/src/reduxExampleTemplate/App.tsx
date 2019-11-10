// import { Dispatch } from 'redux';
// import { connect } from 'react-redux';
// import * as React from 'react';
// import {addNumber} from "./actions";
//
// interface AppProps {
//     numer: number;
//     dispatch: Dispatch<{}>;
// }
//
// interface AppState {
//     numerState: number;
// }
//
// class App extends React.Component<AppProps, AppState> {
//     readonly state ={
//         numerState: 0
//     };
//     render() {
//         const { numer, dispatch } = this.props;
//         console.log("propsy", this.props)
//         return (
//             <div>
//                 <div style={{width: "50px", height: "50px", backgroundColor: "yellow"}} onClick={() => {dispatch(addNumber(this.state.numerState)); this.setState({numerState: numer})}}>{numer}</div>
//             </div>
//         );
//     }
// }
//
// const mapStateToProps = state => {
//     console.log("mapStateToProps", state)
//
//     return {
//         numer: state.mojodos
//     };
// };
//
// // @ts-ignore
// export default connect(mapStateToProps)(App);
