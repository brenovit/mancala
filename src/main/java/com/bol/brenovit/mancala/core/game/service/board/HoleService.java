package com.bol.brenovit.mancala.core.game.service.board;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.bol.brenovit.mancala.core.common.error.ErrorCode;
import com.bol.brenovit.mancala.core.game.common.GameConfigurationConsts;
import com.bol.brenovit.mancala.core.game.dto.Board;
import com.bol.brenovit.mancala.core.game.dto.Hole;
import com.bol.brenovit.mancala.core.game.dto.HoleType;
import com.bol.brenovit.mancala.core.game.dto.GamePlayer;
import com.bol.brenovit.mancala.core.game.exception.SowException;
import com.bol.brenovit.mancala.core.game.linkedlist.Node;

@Service
public class HoleService {

    public boolean isACompleteTurn(int holePosition, Board board, GamePlayer player) {
        Hole hole = board.getHole(holePosition, player).get();
        
        int holeSeeds = hole.getSeeds();
        int boardSize = board.size() - 1;
        
        return holeSeeds >= boardSize;
    }
        
    public Hole distributeHoleSeedsAcrossBoardHoles(int selectedHolePosition, Board board, GamePlayer player) {
        Node lastHole = board.getNode(selectedHolePosition, player)
                .orElseThrow(() -> new SowException(ErrorCode.HOLE_INNEXISTENT));
        
        validateHole(lastHole.getValue());
                
        int remainingSeed = lastHole.getValue().getSeeds();
        
        lastHole.getValue().clean();
        
        do {
            lastHole = lastHole.getNext();
            if(lastHole.getValue().isPot() && 
                    !lastHole.getValue().isOwner(player)) {
                continue;
            }
            remainingSeed--;
            lastHole.getValue().addSeeds(1);
            
        } while (remainingSeed > 0);
        
        return lastHole.getValue();
    }
    
    private void validateHole(Hole hole) {
        if(hole.getSeeds() == 0) {
            throw new SowException(ErrorCode.HOLE_EMPTY);
        }
        
        if(hole.getType() == HoleType.POT) {
            throw new SowException(ErrorCode.HOLE_POT);
        }
    }

    public boolean isAnySideWithAllPitHolesEmpty(Board board) {
        
        Predicate <Hole> isAllPitHolesEmpty = (hole) -> hole.getSeeds() > 0 && hole.isPit();

        Map<GamePlayer, List<Hole>> emptyHolesByPlayer = board.getHoles()
                .stream()
                .collect(Collectors
                        .groupingBy(Hole::getOwner, 
                                Collectors.toList()))
                .entrySet()
                .stream()
                .filter(es -> es.getValue()
                            .stream()
                            .filter(isAllPitHolesEmpty)
                            .collect(Collectors.toList())
                            .isEmpty())
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
                
        return !emptyHolesByPlayer.isEmpty();
    }

    public void captureOpponentOppositeHoleSeeds(int holePosition, Board board, GamePlayer player) {
        Hole hole = board.getOpponentHole(holePosition, player).get();
        
        board.getHole(GameConfigurationConsts.POT_POSITION, player).get()
            .addSeeds(hole.getSeeds());
        
        hole.clean();        
    }
    
    public void moveAllHoleSeedsFromPitToPot(int holePosition, Board board, GamePlayer player) {
        Hole hole = board.getHole(holePosition, player).get();
        
        board.getHole(GameConfigurationConsts.POT_POSITION, player).get()
            .addSeeds(hole.getSeeds());
        
        hole.clean();        
    }

    public void moveAllOpponentSeedsFromPitToPot(Board board, GamePlayer player) {
        
        Map<GamePlayer, List<Hole>> holesByOpponent = board.getHoles()
                .stream()
                .filter(h -> !h.isOwner(player))
                .collect(Collectors
                        .groupingBy(Hole::getOwner, 
                                Collectors.toList()));
        
        holesByOpponent.entrySet()
        .forEach(entry -> {
            int opponentPitSeedsSum = entry.getValue()
                    .stream()
                    .filter(Hole::isPit)
                    .mapToInt(Hole::getSeeds)
                    .sum();
            
            Hole pot = entry.getValue()
                    .stream()
                    .filter(Hole::isPot)
                    .findFirst().get();
            
            pot.addSeeds(opponentPitSeedsSum);
            
            entry.getValue()
                .stream()
                .filter(Hole::isPit)
                .forEach(Hole::clean);
        });
    }
    
    public List<Hole> getPotsWithLargestsAmountOfSeeds(Board board){
        int max = board.getHoles()
                .stream()
                .filter(Hole::isPot)
                .mapToInt(Hole::getSeeds)
                .max()
                .getAsInt();
        
        return board.getHoles()
                .stream()
                .filter(Hole::isPot)
                .filter(h -> h.getSeeds() == max)
                .collect(Collectors.toList());
    }
    
}
