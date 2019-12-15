import styles from "common/topview/resourcecell.module.css";
import React from 'react';
import * as SteelBarsPNG from "static/steel_bars.jpeg";

interface ResourceCellProps {
    amount: number,
    lastIncomeAddition: string,
    incomeRatePerHour: number
}

interface ResourceCellState {
    updatedAmount: number
}

export default class ResourceCell extends React.Component<ResourceCellProps, ResourceCellState> {
    readonly state = {
        updatedAmount: 0,
    };

    public componentDidMount(): void {
        this.setResourceAutoupdater();
    }

    public render() {
        const availableAmount = this.props.amount + this.state.updatedAmount;
        return (
            <div className={styles.cell}>
                Iron {availableAmount.toFixed(0)}
                <div style={{backgroundImage: `url(${SteelBarsPNG})`}} className={styles.planet_image}/>
            </div>
        );
    }

    private setResourceAutoupdater = () => {
        const {lastIncomeAddition, incomeRatePerHour} = this.props;
        const startTimeMilis = Date.parse(lastIncomeAddition);
        const updateAmount = ((Date.now() - startTimeMilis) / 360000) * incomeRatePerHour;
        setInterval(() => {
            this.setState({updatedAmount: this.state.updatedAmount + updateAmount});
        }, 1000)
    }
}

