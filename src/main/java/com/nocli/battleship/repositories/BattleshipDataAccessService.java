package com.nocli.battleship.repositories;

import com.nocli.battleship.Battleship;
import com.nocli.battleship.interfaces.BattleshipDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class BattleshipDataAccessService implements BattleshipDao {
    private Map<String, Battleship> battleshipGames;

    public BattleshipDataAccessService() { this.battleshipGames = new HashMap<>(); }

    @Override
    public Map<String, Battleship> selectAllBattleshipGames() {
        return battleshipGames;
    }

    @Override
    public Optional<Battleship> selectBattleshipGameById(String gameId) {
        return Optional.ofNullable(battleshipGames.get(gameId));
    }

    @Override
    public void addBattleshipGame(String gameId, Battleship battleshipGame) {
        battleshipGames.put(gameId, battleshipGame);
    }

    @Override
    public void deleteBattleshipGameById(String gameId) {
        Optional<Battleship> battleship = selectBattleshipGameById(gameId);
        if (battleship.isPresent()) battleshipGames.remove(gameId);
    }
}
