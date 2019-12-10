import styles from "common/topview/resourcebar.module.css";
import React from 'react';
import * as SteelBarsPNG from "static/steel_bars.jpeg";

interface ResourceCellProps {
    amount: number,
    lastIncomeAddition: string,
    incomeRatePerHour: number
}

interface ResourceCellState {
    updatedAmount: number,
    lastIncomeAddition: string | null,
    incomeRatePerHour: number
}

export default class ResourceCell extends React.Component<ResourceCellProps, ResourceCellState> {
    readonly state = {
        updatedAmount: 0,
        lastIncomeAddition: new Date().toDateString(),
        incomeRatePerHour: 0
    };

    public componentDidMount(): void {
        this.setResourceAutoupdater();
    }

    public static getDerivedStateFromProps({lastIncomeAddition, amount, incomeRatePerHour}: ResourceCellProps, state: ResourceCellState) {
        return {updatedAmount: amount, lastIncomeAddition, incomeRatePerHour}
    }


    public render() {
        const {updatedAmount} = this.state;
        return (
            <div>
                Iron {updatedAmount && updatedAmount.toFixed(0)}
                <div style={{backgroundImage: `url(${SteelBarsPNG})`}} className={styles.planet_image}/>
            </div>
        );
    }

    private setResourceAutoupdater() {
        const {lastIncomeAddition, incomeRatePerHour} = this.state;
        const {amount} = this.props;
        const startTimeMilis = Date.parse(lastIncomeAddition);
        setInterval(() => {
            const updateAmount = ((Date.now() - startTimeMilis) / 3600000) * incomeRatePerHour;
            this.setState({updatedAmount: amount + updateAmount});
            console.info(amount + updateAmount)
        }, 1000)
    }
}

