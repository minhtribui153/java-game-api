package com.nocli.battleship.records.information;

import com.nocli.battleship.records.request.GuildRequest;
import com.nocli.battleship.records.response.BoardResponse;

public record ReadyInformation(
        boolean player1Ready,
        boolean player2Ready,
        PlayerInformation player1,
        PlayerInformation player2,
        String player1MessageId,
        String player2MessageId,
        GuildRequest guild
) {
}
