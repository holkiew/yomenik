import styles from "common/topview/resourcecell.module.css";
import React from 'react';
import * as SteelBarsPNG from "static/steel_bars.jpeg";

interface ResourceCellProps {
    amount: number,
    lastIncomeAddition: string,
    incomeRatePerHour: number,
    intervalInMs: number
}

interface ResourceCellState {
    updatedAmount: number,
    updatePerInterval: number
    incomeRatePerHour: number
}

export default class ResourceCell extends React.Component<ResourceCellProps, ResourceCellState> {
    readonly state = {
        updatedAmount: 0,
        updatePerInterval: 0,
        incomeRatePerHour: 0
    };

    static getDerivedStateFromProps(props: ResourceCellProps, state: ResourceCellState) {
        if (props.incomeRatePerHour !== state.incomeRatePerHour) {
            return {
                incomeRatePerHour: props.incomeRatePerHour,
                updatePerInterval: props.incomeRatePerHour / (3.6 * props.intervalInMs)
            }
        }
        return null;
    }

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
        const {lastIncomeAddition, incomeRatePerHour, intervalInMs} = this.props;
        const startTimeMilis = Date.parse(lastIncomeAddition);
        const updateAmountTillNow = ((Date.now() - startTimeMilis) / 360000) * incomeRatePerHour;
        console.info(lastIncomeAddition)
        this.setState({
            updatedAmount: this.state.updatedAmount + updateAmountTillNow,
            incomeRatePerHour,
            updatePerInterval: incomeRatePerHour / (3.6 * intervalInMs)
        });
        setInterval(() => {
            this.setState({updatedAmount: this.state.updatedAmount + this.state.updatePerInterval});
        }, intervalInMs)
    };
}

