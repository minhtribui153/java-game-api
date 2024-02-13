package com.nocli.battleship.records.response;

import com.nocli.battleship.records.request.GuildRequest;

public record BattleshipDeleteResponse(
        String player1Id,
        String player2Id,
        String player1MessageId,
        String player2MessageId,
        GuildRequest guild
) {
}
