package com.nocli.battleship.services;

import com.nocli.battleship.Battleship;
import com.nocli.battleship.interfaces.BattleshipDao;
import com.nocli.battleship.records.information.*;
import com.nocli.battleship.records.request.*;
import com.nocli.battleship.records.response.BattleshipDeleteResponse;
import com.nocli.battleship.records.response.BattleshipResponse;
import com.nocli.battleship.records.response.ManageShipResponse;
import com.nocli.battleship.records.response.ShootResponse;
import com.nocli.exception.InvalidRequestException;
import com.nocli.exception.ResourceNotFoundException;
import com.nocli.ship.Ship;
import com.nocli.ship.services.ShipService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BattleshipService {
    private BattleshipDao battleshipDao;
    private ShipService shipService;

    public BattleshipService(BattleshipDao battleshipDao, ShipService shipService) {
        this.battleshipDao = battleshipDao;
        this.shipService = shipService;
    }

    public Map<String, Battleship> getAllBattleshipGames() {
        return this.battleshipDao.selectAllBattleshipGames();
    }

    public Battleship getBattleshipGameById(String gameId) {
        return this.battleshipDao.selectBattleshipGameById(gameId)
                .orElseThrow(() -> new ResourceNotFoundException("Battleship game with ID \"%s\" not found".formatted(gameId)));
    }

    public BattleshipDeleteResponse deleteBattleshipGameById(String gameId) {
        Battleship battleshipGame = getBattleshipGameById(gameId);

        battleshipDao.deleteBattleshipGameById(gameId);

        return new BattleshipDeleteResponse(
                battleshipGame.getPlayer1Id(),
                battleshipGame.getPlayer2Id(),
                battleshipGame.getPlayer1MessageId(),
                battleshipGame.getPlayer2MessageId(),
                battleshipGame.getGuild()
        );
    }

    public Optional<Battleship> getCurrentBattleshipGame(String playerId) {
        return battleshipDao.selectAllBattleshipGames()
                .values()
                .stream()
                .filter(battleship ->
                        battleship.getPlayer1Id().equals(playerId) ||
                                battleship.getPlayer2Id().equals(playerId))
                .findFirst();
    }

    public BattleshipResponse createBattleshipGame(BattleshipRequest battleshipRequest) {
        Optional<Battleship> player1CurrentGame = getCurrentBattleshipGame(battleshipRequest.player1Id());
        if (player1CurrentGame.isPresent()) throw new InvalidRequestException("You are already in a game");

        Optional<Battleship> player2CurrentGame = getCurrentBattleshipGame(battleshipRequest.player2Id());
        if (player2CurrentGame.isPresent()) throw new InvalidRequestException("Player \"%s\" is already in a game".formatted(battleshipRequest.player2Id()));

        if (battleshipRequest.player1Id().equals(battleshipRequest.player2Id())) throw new InvalidRequestException("Cannot play against yourself");

        List<Ship> shipList = shipService.getAllShips();

        Ship[] ships = new Ship[shipList.size()];

        for (int i = 0; i < ships.length; i++) ships[i] = shipList.get(i);

        Battleship battleship = new Battleship(battleshipRequest.player1Id(), battleshipRequest.player2Id(), ships, new GuildRequest("", "", " "), "", "");

        String gameId = String.valueOf(UUID.randomUUID());

        battleshipDao.addBattleshipGame(gameId, battleship);

        return new BattleshipResponse(
                battleshipRequest.player1Id(),
                battleshipRequest.player2Id(),
                battleship.getBoardById(1).getBoard(),
                battleship.getBoardById(2).getBoard(),
                gameId,
                shipService.getAllShips()
        );
    }

    public PlayerInformation getPlayerInformationByPlayerNum(String gameId, Integer playerNum) {
        Battleship battleshipGame = getBattleshipGameById(gameId);

        String id = playerNum == 1 ? battleshipGame.getPlayer1Id() : battleshipGame.getPlayer2Id();

        List<ShipInformation> usedShips = battleshipGame.getBoardById(playerNum).getHealthMap()
                .values().stream().toList();

        return new PlayerInformation(
                id,
                battleshipGame.getBoardById(playerNum).getBoard(),
                battleshipGame.getBoardById(playerNum).getAttackBoard(),
                usedShips,
                battleshipGame.getBoardById(playerNum).getRemainingShips()
        );
    }

    public void setConfigurations(String gameId, BattleshipConfigurationRequest battleshipConfigurationRequest) {
        Battleship battleshipGame = getBattleshipGameById(gameId);
        battleshipGame.setPlayer1MessageId(battleshipConfigurationRequest.player1MessageId());
        battleshipGame.setPlayer2MessageId(battleshipConfigurationRequest.player2MessageId());
        battleshipGame.setGuild(battleshipConfigurationRequest.guild());
    }

    public ManageShipResponse placeShip(String gameId, Integer playerNum, AddShipRequest addShipRequest) {
        Battleship battleshipGame = getBattleshipGameById(gameId);
        Ship ship = shipService.getShipById(addShipRequest.id());

        if (playerNum != 1 && playerNum != 2)
            throw new InvalidRequestException("Invalid playerNumber \"%s\"".formatted(playerNum));

        battleshipGame.placeShip(ship.getName(), addShipRequest.x(), addShipRequest.y(), addShipRequest.direction(), playerNum);

        return new ManageShipResponse(
                playerNum,
                battleshipGame.getPlayerIdByNum(playerNum),
                battleshipGame.getBoardById(playerNum).getBoard(),
                battleshipGame.getBoardById(playerNum).getUsedShips(),
                battleshipGame.getBoardById(playerNum).getRemainingShips()
        );
    }

    public ManageShipResponse getBoard(String gameId, String playerId) {
        Battleship battleshipGame = getBattleshipGameById(gameId);

        int playerNum = battleshipGame.getPlayerNumById(playerId);

        return new ManageShipResponse(
                playerNum,
                playerId,
                battleshipGame.getBoardById(playerNum).getBoard(),
                battleshipGame.getBoardById(playerNum).getUsedShips(),
                battleshipGame.getBoardById(playerNum).getRemainingShips()
        );
    }

    public ManageShipResponse getBoardById(String gameId, Integer playerNum) {
        Battleship battleshipGame = getBattleshipGameById(gameId);

        return new ManageShipResponse(
                playerNum,
                battleshipGame.getPlayerIdByNum(playerNum),
                battleshipGame.getBoardById(playerNum).getBoard(),
                battleshipGame.getBoardById(playerNum).getUsedShips(),
                battleshipGame.getBoardById(playerNum).getRemainingShips()
        );
    }

    public ManageShipResponse getAttackBoardById(String gameId, Integer playerNum) {
        Battleship battleshipGame = getBattleshipGameById(gameId);

        return new ManageShipResponse(
                playerNum,
                battleshipGame.getPlayerIdByNum(playerNum),
                battleshipGame.getBoardById(playerNum).getAttackBoard(),
                battleshipGame.getBoardById(playerNum).getUsedShips(),
                battleshipGame.getBoardById(playerNum).getRemainingShips()
        );
    }

    public ReadyInformation playerReady(String gameId, Integer playerNum) {
        Battleship battleshipGame = getBattleshipGameById(gameId);

        List<Ship> usedShips = battleshipGame.getBoardById(playerNum).getUsedShips();

        if (usedShips.size() < 4) throw new InvalidRequestException("You need at least 4 ships placed on the board to start the game");

        battleshipGame.setReady(playerNum);

        return new ReadyInformation(
                battleshipGame.getReady(1),
                battleshipGame.getReady(2),
                new PlayerInformation(
                        battleshipGame.getPlayer1Id(),
                        battleshipGame.getBoardById(1).getBoard(),
                        battleshipGame.getBoardById(1).getAttackBoard(),
                        battleshipGame.getBoardById(1).getUsedShips().stream().map(ship -> new ShipInformation(ship, false, ship.getLength())).toList(),
                        battleshipGame.getBoardById(1).getRemainingShips()
                ),
                new PlayerInformation(
                        battleshipGame.getPlayer2Id(),
                        battleshipGame.getBoardById(2).getBoard(),
                        battleshipGame.getBoardById(2).getAttackBoard(),
                        battleshipGame.getBoardById(2).getUsedShips().stream().map(ship -> new ShipInformation(ship, false, ship.getLength())).toList(),
                        battleshipGame.getBoardById(2).getRemainingShips()
                ),
                battleshipGame.getPlayer1MessageId(),
                battleshipGame.getPlayer2MessageId(),
                battleshipGame.getGuild()
        );
    }

    public ManageShipResponse deleteShip(String gameId, Integer playerNum, Integer shipId) {
        Battleship battleshipGame = getBattleshipGameById(gameId);
        Ship ship = shipService.getShipById(shipId);

        battleshipGame.removeShip(ship.getName(), playerNum);

        return new ManageShipResponse(
                playerNum,
                battleshipGame.getPlayerIdByNum(playerNum),
                battleshipGame.getBoardById(playerNum).getBoard(),
                battleshipGame.getBoardById(playerNum).getUsedShips(),
                battleshipGame.getBoardById(playerNum).getRemainingShips()
        );
    }

    public ShootResponse fire(String gameId, Integer playerNum, ShootRequest shootRequest) {
        Battleship battleshipGame = getBattleshipGameById(gameId);
        if (!battleshipGame.getReady(1) && !battleshipGame.getReady(2)) throw new InvalidRequestException("Players are not ready to play yet");

        AttackStatus status = battleshipGame.fire(shootRequest.x(), shootRequest.y(), playerNum == 1 ? 2 : 1);

        PlayerInformation player1 = getPlayerInformationByPlayerNum(gameId, 1);
        PlayerInformation player2 = getPlayerInformationByPlayerNum(gameId, 2);

        return new ShootResponse(status.hit(), status.hitShip(), status.deadShip(), new DefaultInformation(battleshipGame.getGuild(), battleshipGame.getPlayer1MessageId(), battleshipGame.getPlayer2MessageId(), player1, player2));
    }
}
