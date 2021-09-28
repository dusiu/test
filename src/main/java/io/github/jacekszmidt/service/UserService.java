package io.github.jacekszmidt.service;

import io.github.jacekszmidt.model.User;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final List<User> USERS = new ArrayList<>();

    public void confirmData() {
        System.out.print("Czy wszystkie dane sie zgadzaja? Y/N? ");
        String checkData = SCANNER.nextLine();
        if (checkData.equals("Y") || checkData.equals("y")) {
            LOGGER.info("Dane zatwierdzone!");
        } else if (checkData.equals("N") || checkData.equals("n")) {
            LOGGER.info("Wprowadzone dane nie zostaly zatwierdzone");
        }
    }

    public void addUser() {
        User user = addNewUser();
        USERS.add(user);
        LOGGER.info("Dodano nowego uzytkownika: {}", user);
        confirmData();
    }

    private User addNewUser() {
        LOGGER.info("Podaj imie klienta:");
        String name = SCANNER.nextLine();
        LOGGER.info("Podaj nazwisko klienta:");
        String lastName = SCANNER.nextLine();
        LOGGER.info("Podaj numer telefonu klienta:");
        String phoneNumber = SCANNER.nextLine();

        return new User(name, lastName, phoneNumber);
    }

    public void showUsers() {
        if (USERS.isEmpty()) {
            LOGGER.info("Nie ma dodanych uzytkownikow!");
            return;
        }
        LOGGER.info("Lista uzytkownikow:");
        for (User user : USERS) {
            LOGGER.info("{}: {}", USERS.indexOf(user), user);
        }
    }

    public User getUser() {
        if (USERS.isEmpty()) {
            LOGGER.info("Nie ma uzytkownikow, najpierw ich dodaj!");
            LOGGER.info("Przekierowywanie...");
            addUser();

        }
        showUsers();
        LOGGER.info("Wybierz uzytkownika: ");
        String s = SCANNER.nextLine();
        while (!NumberUtils.isParsable(s) || Integer.parseInt(s) < 0 || Integer.parseInt(s) > USERS.size()) {
            LOGGER.info("Zly uzytkownik, sprobuj jeszcze raz!");
            s = SCANNER.nextLine();
        }
        return USERS.get(Integer.parseInt(s));
    }
}
