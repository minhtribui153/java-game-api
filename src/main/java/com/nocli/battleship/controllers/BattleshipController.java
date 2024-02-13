package com.nocli.battleship.controllers;

import com.nocli.battleship.Battleship;
import com.nocli.battleship.records.information.*;
import com.nocli.battleship.records.request.*;
import com.nocli.battleship.records.response.*;
import com.nocli.battleship.services.BattleshipService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/v1/battleships")
public class BattleshipController {
    private BattleshipService battleshipService;

    public BattleshipController(BattleshipService battleshipService) {
        this.battleshipService = battleshipService;
    }

    @GetMapping
    Map<String, Battleship> getAllBattleshipGames() {
        return battleshipService.getAllBattleshipGames();
    }

    @PostMapping
    BattleshipResponse createNewBattleshipGame(@RequestBody BattleshipRequest battleshipRequest) {
        return this.battleshipService.createBattleshipGame(battleshipRequest);
    }

    @GetMapping("{id}")
    Battleship getBattleshipGameById(@PathVariable("id") String id) {
        return battleshipService.getBattleshipGameById(id);
    }

    @PostMapping("{id}")
    void setConfigurations(@PathVariable("id") String id, @RequestBody BattleshipConfigurationRequest request) {
        battleshipService.setConfigurations(id, request);
    }

    @DeleteMapping("{id}")
    BattleshipDeleteResponse deleteBattleshipGameById(@PathVariable("id") String id) {
        return battleshipService.deleteBattleshipGameById(id);
    }

    @GetMapping("{id}/boards")
    BoardResponse getPlayerBoards(@PathVariable("id") String id) {
        Battleship battleshipGame = battleshipService.getBattleshipGameById(id);

        return new BoardResponse(
                battleshipGame.getBoardById(1).getBoard(),
                battleshipGame.getBoardById(2).getBoard(),
                battleshipGame.getBoardById(1).getRemainingShips(),
                battleshipGame.getBoardById(2).getRemainingShips()
        );
    }

    @GetMapping("{id}/attackboards")
    BoardResponse getPlayerAttackBoards(@PathVariable("id") String id) {
        Battleship battleshipGame = battleshipService.getBattleshipGameById(id);

        return new BoardResponse(
                battleshipGame.getBoardById(1).getAttackBoard(),
                battleshipGame.getBoardById(2).getAttackBoard(),
                battleshipGame.getBoardById(1).getRemainingShips(),
                battleshipGame.getBoardById(2).getRemainingShips()
        );
    }

    @GetMapping("{id}/players/{playerNumber}")
    PlayerInformation getPlayerInformationByPlayerNum(
            @PathVariable("id") String id,
            @PathVariable("playerNumber") Integer playerNum
    ) {
        return battleshipService.getPlayerInformationByPlayerNum(id, playerNum);
    }

    @PostMapping("{id}/players/{playerNumber}/placeship")
    ManageShipResponse placeShip(@PathVariable("id") String id, @PathVariable("playerNumber") Integer playerNum, @RequestBody AddShipRequest addShipRequest) {
        return battleshipService.placeShip(id, playerNum, addShipRequest);
    }

    @PostMapping("{id}/players/{playerNumber}/ready")
    ReadyInformation ready(@PathVariable("id") String id, @PathVariable("playerNumber") Integer playerNum) {
        return battleshipService.playerReady(id, playerNum);
    }

    @PostMapping("{id}/players/{playerNumber}/fire")
    ShootResponse fireAtOpponentBase(@PathVariable("id") String id, @PathVariable("playerNumber") Integer playerNum, @RequestBody ShootRequest request) {
        return battleshipService.fire(id, playerNum, request);
    }

    @DeleteMapping("{id}/players/{playerNumber}/removeship/{shipId}")
    ManageShipResponse removeShip(@PathVariable("id") String id, @PathVariable("playerNumber") Integer playerNum, @PathVariable("shipId") Integer shipId) {
        return battleshipService.deleteShip(id, playerNum, shipId);
    }

    @GetMapping("{id}/players/{playerNumber}/board")
    ManageShipResponse getBoardById(@PathVariable("id") String id, @PathVariable("playerNumber") Integer playerId) {
        return battleshipService.getBoardById(id, playerId);
    }

    @GetMapping("{id}/players/{playerNumber}/attackboard")
    ManageShipResponse getAttackBoardById(@PathVariable("id") String id, @PathVariable("playerNumber") Integer playerId) {
        return battleshipService.getAttackBoardById(id, playerId);
    }

    @GetMapping("{id}/player/{playerId}/board")
    ManageShipResponse getBoards(@PathVariable("id") String id, @PathVariable("playerId") String playerId) {
        return battleshipService.getBoard(id, playerId);
    }
}
