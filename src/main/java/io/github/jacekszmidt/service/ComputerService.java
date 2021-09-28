package io.github.jacekszmidt.service;

import io.github.jacekszmidt.model.PersonalComputer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ComputerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ComputerService.class);
    private static final List<PersonalComputer> PERSONAL_COMPUTERS = new ArrayList<>();
    private static final Scanner SCANNER = new Scanner(System.in);

    public PersonalComputer getPersonalComputer() {
        LOGGER.info("Podaj model procesora: ");
        String cpu = SCANNER.nextLine();
        LOGGER.info("Podaj model plyty glownej: ");
        String motherBoard = SCANNER.nextLine();
        LOGGER.info("Podaj model pamieci RAM: ");
        String memoryRam = SCANNER.nextLine();
        LOGGER.info("Podaj model zasilacza: ");
        String psu = SCANNER.nextLine();
        LOGGER.info("Podaj model dysku: ");
        String disc = SCANNER.nextLine();
        LOGGER.info("Inne?");
        String otherInfo = SCANNER.nextLine();
        LOGGER.info("Opis usterki");
        String problemDescription = SCANNER.nextLine();
        LOGGER.info("Czy mozna usuwac dane? ");
        String cleanData = SCANNER.nextLine();
        return new PersonalComputer(cpu, motherBoard, memoryRam, psu, disc, otherInfo, problemDescription, cleanData);
    }

    public void addNewComputer() {
        PersonalComputer personalComputer = getPersonalComputer();
        PERSONAL_COMPUTERS.add(personalComputer);
        LOGGER.info("Personal computer was added: {}", personalComputer);
    }

    public void showComputers() {
        if (PERSONAL_COMPUTERS.isEmpty()) {
            LOGGER.info("There are no personal computers");
            return;
        }
        LOGGER.info("List of all computer:");
        for (PersonalComputer personalComputer : PERSONAL_COMPUTERS) {
            LOGGER.info("{}: {}", PERSONAL_COMPUTERS.indexOf(personalComputer), personalComputer);
        }
    }
}
