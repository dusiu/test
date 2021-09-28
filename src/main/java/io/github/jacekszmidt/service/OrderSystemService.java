package io.github.jacekszmidt.service;

import io.github.jacekszmidt.model.ExcelEntity;
import io.github.jacekszmidt.repository.ExcelRepository;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.stream.Collectors;

@Service
public class OrderSystemService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderSystemService.class);
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final ComputerService COMPUTER_SERVICE = new ComputerService();
    private static final UserService USER_SERVICE = new UserService();
    private static final LaptopService LAPTOP_SERVICE = new LaptopService();
    private static final ComputerOutputWriter COMPUTER_OUTPUT_WRITER = new ExcelComputerOutputWriter();
    private static final Queue<Pair<String, byte[]>> EXCEL_FILE_NAME_WITH_CONTENT_QUEUE = new LinkedList<>();
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    @Autowired
    private ExcelRepository excelRepository;

    @PostConstruct
    private void startOrderSystem() throws IOException {
        boolean exit = false;
        while (!exit) {
            printMainMenu();
            int userChoice = getUserChoice();
            switch (userChoice) {
                case 0:
                    exit = true;
                    break;
                case 1:
                    USER_SERVICE.addUser();
                    break;
//                case 2:
//                    LAPTOP_SERVICE.addNewLaptop();
//                    break;
//                case 3:
//                    COMPUTER_SERVICE.addNewComputer();
//                    break;
                case 4:
                    USER_SERVICE.showUsers();
                    break;
//                case 5:
//                    LAPTOP_SERVICE.showLaptops();
//                    break;
//                case 6:
//                    COMPUTER_SERVICE.showComputers();
//                    break;
                case 7:
                    Pair<String, byte[]> laptopExcelFile = COMPUTER_OUTPUT_WRITER.writeOutput(USER_SERVICE.getUser(), LAPTOP_SERVICE.getLaptop());
                    if (laptopExcelFile != null) {
                        EXCEL_FILE_NAME_WITH_CONTENT_QUEUE.add(laptopExcelFile);
                    }
                    break;
                case 8:
                    Pair<String, byte[]> pcExcelFile = COMPUTER_OUTPUT_WRITER.writeOutput(USER_SERVICE.getUser(), COMPUTER_SERVICE.getPersonalComputer());
                    if (pcExcelFile != null) {
                        EXCEL_FILE_NAME_WITH_CONTENT_QUEUE.add(pcExcelFile);
                    }
                    break;
                case 9:
                    if (EXCEL_FILE_NAME_WITH_CONTENT_QUEUE.isEmpty()) {
                        LOGGER.warn("Excel file queue is empty");
                    } else {
                        Pair<String, byte[]> excelFileNameWithContent = EXCEL_FILE_NAME_WITH_CONTENT_QUEUE.poll();
                        saveExcelEntity(new ExcelEntity(excelFileNameWithContent.getKey(), USER_SERVICE.getUser().getName(),
                                excelFileNameWithContent.getValue()));
                        break;
//                        try (FileOutputStream file = new FileOutputStream(excelFileNameWithContent.getKey())) {
//                            file.write(excelFileNameWithContent.getValue());
//                            LOGGER.info("Saved file: {}", excelFileNameWithContent.getKey());
//                        } catch (IOException e) {
//                            LOGGER.error("Error during saving file: {}", e.getMessage());
//                        }
                    }
                case 10:
                    LOGGER.info("List of all files in db:");
                    for (ExcelEntity excelEntity : excelRepository.findAll()) {
                        LOGGER.info("{}", excelEntity);
                    }
                    break;
                case 11:
                    LOGGER.info("Choose excel file id:");
                    Long excelFileId = getExcelFleId();
                    while (!excelRepository.existsById(excelFileId)) {
                        LOGGER.info("Not existing excel file id!");
                        excelFileId = getExcelFleId();
                    }
                    ExcelEntity excel = excelRepository.findById(excelFileId).get();
                    File diskFile = new File(excel.getFileName());
                    if (diskFile.exists()) {
                        LOGGER.info("Given file already exist on disk, overwriting");
                        diskFile.delete();
                    }
                    Files.write(diskFile.toPath(), excel.getFile(), StandardOpenOption.CREATE);
                    LOGGER.info("Saved file: {} on disk, location: {}", excel.getFileName(), diskFile.toPath().toAbsolutePath());
                    break;
            }

        }
    }

    private Long getExcelFleId() {
        String s = SCANNER.nextLine();
        while (!NumberUtils.isParsable(s)) {
            LOGGER.info("Wrong id, id should be number which exist in db");
            s = SCANNER.nextLine();
        }
        return Long.valueOf(s);
    }

    private int getUserChoice() {
        while (true) {
            String choice = SCANNER.nextLine();
            if (!NumberUtils.isParsable(choice) || Integer.parseInt(choice) > 11 || Integer.parseInt(choice) < 0) {
                LOGGER.info("Choose correct user number");
                continue;
            }
            return Integer.parseInt(choice);
        }
    }

    private void printMainMenu() {
        String mainMenu = "\n0: exit" + System.lineSeparator() +
                "1: add user" + System.lineSeparator() +
//                "2: add laptop" + System.lineSeparator() +
//                "3: add computer" + System.lineSeparator() +
                "4: show users" + System.lineSeparator() +
                "5: show laptops" + System.lineSeparator() +
                "6: show computers" + System.lineSeparator() +
                "7: assign laptop to the user" + System.lineSeparator() +
                "8: assign PC to the user" + System.lineSeparator() +
                "9: save user report excel file" + System.lineSeparator() +
                "10: show all excel files in database" + System.lineSeparator() +
                "11: download excel file from database" + System.lineSeparator();

        LOGGER.info(mainMenu);
    }

    public void saveExcelEntity(ExcelEntity excelEntity) {
        if (validator.validate(excelEntity).isEmpty()) {
            LOGGER.info("Saved excel entity with id: {}", excelRepository.save(excelEntity).getId());
        } else {
            LOGGER.error("Excel Entity: {}, Validation exception: {}", excelEntity, validator.validate(excelEntity).stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", ")));
        }
    }

}