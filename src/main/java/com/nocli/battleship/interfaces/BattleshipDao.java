package com.nocli.battleship.interfaces;

import com.nocli.battleship.Battleship;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BattleshipDao {
    Map<String, Battleship> selectAllBattleshipGames();
    Optional<Battleship> selectBattleshipGameById(String gameId);
    void addBattleshipGame(String gameId, Battleship battleshipGame);
    void deleteBattleshipGameById(String gameId);
}
