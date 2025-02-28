package com.example.mud.controller;

import java.util.Scanner;
import com.example.mud.player.Player;
import com.example.mud.room.Room;
import com.example.mud.item.Item;

/**
 * MUDController:
 * Контроллер для обработки команд игрока в MUD-игре.
 */
public class MUDController {
    private final Player player;
    private boolean running;
    private final Scanner scanner;

    /**
     * Конструктор инициализирует контроллер с игроком.
     */
    public MUDController(Player player) {
        this.player = player;
        this.running = true;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Основной игровой цикл.
     */
    public void runGameLoop() {
        System.out.println("Добро пожаловать в MUD! Введите 'help' для списка команд.");
        while (running) {
            System.out.print("> ");
            String input = scanner.nextLine().trim().toLowerCase();
            handleInput(input);
        }
    }

    /**
     * Обрабатывает команды игрока.
     */
    public void handleInput(String input) {
        String[] parts = input.split(" ", 2);
        String command = parts[0];
        String argument = (parts.length > 1) ? parts[1] : "";

        switch (command) {
            case "look":
                lookAround();
                break;
            case "move":
                move(argument);
                break;
            case "pick":
                if (argument.startsWith("up ")) {
                    pickUp(argument.substring(3));
                } else {
                    System.out.println("Использование: pick up <itemName>");
                }
                break;
            case "inventory":
                checkInventory();
                break;
            case "help":
                showHelp();
                break;
            case "quit":
            case "exit":
                running = false;
                System.out.println("Выход из игры...");
                break;
            default:
                System.out.println("Неизвестная команда");
        }
    }

    /**
     * Описывает текущую комнату.
     */
    private void lookAround() {
        Room currentRoom = player.getCurrentRoom();
        System.out.println(currentRoom.getDescription());
        System.out.println("Предметы: " + currentRoom.listItems());
    }

    /**
     * Перемещает игрока в указанном направлении.
     */
    private void move(String direction) {
        Room nextRoom = player.getCurrentRoom().getConnectedRoom(direction);
        if (nextRoom != null) {
            player.setCurrentRoom(nextRoom);
            System.out.println("Вы переместились в: " + nextRoom.getDescription());
        } else {
            System.out.println("Вы не можете идти туда!");
        }
    }

    /**
     * Подбирает предмет в комнате.
     */
    private void pickUp(String itemName) {
        Room currentRoom = player.getCurrentRoom();
        Item item = currentRoom.removeItem(itemName);
        if (item != null) {
            player.addItem(item);
            System.out.println("Вы подобрали: " + itemName);
        } else {
            System.out.println("Здесь нет предмета с именем " + itemName + "!");
        }
    }

    /**
     * Показывает инвентарь игрока.
     */
    private void checkInventory() {
        System.out.println("Ваш инвентарь: " + player.listInventory());
    }

    /**
     * Показывает список доступных команд.
     */
    private void showHelp() {
        System.out.println("Доступные команды:");
        System.out.println("look - осмотреть комнату");
        System.out.println("move <forward|back|left|right> - двигаться в указанном направлении");
        System.out.println("pick up <itemName> - подобрать предмет");
        System.out.println("inventory - посмотреть инвентарь");
        System.out.println("help - список команд");
        System.out.println("quit / exit - выход из игры");
    }
}
