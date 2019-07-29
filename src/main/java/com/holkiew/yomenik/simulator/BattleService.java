package com.holkiew.yomenik.simulator;

import com.holkiew.yomenik.simulator.dto.NewBattleRequest;
import com.holkiew.yomenik.simulator.persistence.BattleHistory;
import com.holkiew.yomenik.simulator.persistence.BattleHistoryRepository;
import com.holkiew.yomenik.simulator.persistence.BattleRecap;
import com.holkiew.yomenik.util.UtilMethods;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SynchronousSink;

import java.rmi.activation.UnknownObjectException;
import java.time.LocalDateTime;
import java.util.function.BiConsumer;
import java.util.function.Function;


@Service
@RequiredArgsConstructor
public class BattleService {

    private final BattleHistoryRepository repository;

    public void newBattle(NewBattleRequest request){
        var army1 = Army.of(request.getArmy1());
        var army2 = Army.of(request.getArmy2());
        resolveBattle(army1, army2);
    }

    public void resolveBattle(Army army1, Army army2){
        long stageDelay = 10;
        BattleStrategy battleStrategy = BattleStrategy.of(army1, army2);
        LocalDateTime time = LocalDateTime.now();
        var battleHistory = new BattleHistory(battleStrategy, time);
        while(!battleStrategy.isBattleEnded()){
            battleStrategy.resolveRound();
            time = time.plusSeconds(stageDelay);
            battleHistory.addNewEntry(battleStrategy, time);
        }
        battleHistory.setEndDate(time);
        repository.save(battleHistory).doOnError(Throwable::printStackTrace).subscribe();
    }

    public Mono<BattleHistory> getCurrentBattleHistory(){
        return repository.findFirstByIsIssuedFalseOrderByStartDate()
                .handle(updateIssuedBattles())
                .retry(UnknownObjectException.class::isInstance)
                .cast(BattleHistory.class)
                .switchIfEmpty(repository.findFirstByIsIssuedTrueOrderByStartDateDesc())
                .map(filterOutFutureResults());
    }

    private BiConsumer<BattleHistory, SynchronousSink<Object>> updateIssuedBattles() {
        return (battleHistory, synchronousSink) -> {
            var currentTime = LocalDateTime.now();
            var isIssued = battleHistory.getEndDate().isBefore(currentTime);
            battleHistory.setIsIssued(isIssued);
            if (isIssued) {
                // TODO customowy ex
                repository.save(battleHistory).subscribe();
                synchronousSink.error(new UnknownObjectException("Object has been issued"));
            } else {
                battleHistory.getBattleRecapMap().values().stream()
                        .filter(BattleRecap::getIsNotIssued)
                        .forEach(battleRecap -> battleRecap.setIsIssued(battleRecap.getIssueTime().isBefore(currentTime)));
                repository.save(battleHistory).subscribe();
                synchronousSink.next(battleHistory);
            }
        };
    }

    private Function<BattleHistory, BattleHistory> filterOutFutureResults() {
        return battleHistory -> {
            if (!battleHistory.getIsIssued()) {
                var filteredEnumMap = battleHistory.getBattleRecapMap()
                        .entrySet()
                        .stream()
                        .filter(entry -> entry.getValue().getIsIssued())
                        .collect(UtilMethods.toEnumMap(BattleStage.class));
                battleHistory.setBattleRecapMap(filteredEnumMap);
            }
            return battleHistory;
        };
    }

}
