package com.nocli.ship;

import jakarta.persistence.*;

import java.util.Objects;

@Entity(name = "battleship_ships")
public class Ship {
    @Id
    @SequenceGenerator(name = "ship_id_sequence", sequenceName = "ship_id_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ship_id_sequence")
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int length;

    @Column(nullable = false)
    private String shipId;

    public Ship(int id, String name, int length, String shipId) {
        this.id = id;
        this.name = name;
        this.length = length;
        this.shipId = shipId;
    }

    public Ship(String name, int length, String shipId) {
        this.name = name;
        this.length = length;
        this.shipId = shipId;
    }

    public Ship() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getShipId() {
        return shipId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setShipId(String shipId) {
        this.shipId = shipId;
    }

    @Override
    public String toString() {
        return "Ship{" +
                "name='" + name + '\'' +
                ", length=" + length +
                ", shipId=" + shipId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ship ship = (Ship) o;
        return length == ship.length && shipId == ship.shipId && Objects.equals(name, ship.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, length, shipId);
    }
}
