package com.nocli.battleship.records.request;

public record AddShipRequest(
        Integer id,
        Integer x,
        Integer y,
        String direction
) {}
