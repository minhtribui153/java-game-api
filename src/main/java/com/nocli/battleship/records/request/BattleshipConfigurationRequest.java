package com.nocli.battleship.records.request;

public record BattleshipConfigurationRequest(
        String player1MessageId,
        String player2MessageId,
        GuildRequest guild
) {
}
