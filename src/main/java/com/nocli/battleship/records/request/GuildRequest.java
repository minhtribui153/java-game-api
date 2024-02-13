package com.nocli.battleship.records.request;

public record GuildRequest(
        String guildId,
        String channelId,
        String messageId
) {
}
