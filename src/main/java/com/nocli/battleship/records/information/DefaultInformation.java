package com.nocli.battleship.records.information;

import com.nocli.battleship.records.request.GuildRequest;

public record DefaultInformation(
        GuildRequest guild,
        String player1MessageId,
        String player2MessageId,
        PlayerInformation player1,
        PlayerInformation player2
) {}
