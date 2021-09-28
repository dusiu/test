package io.github.jacekszmidt.service;

import io.github.jacekszmidt.model.Laptop;
import io.github.jacekszmidt.model.PersonalComputer;
import io.github.jacekszmidt.model.User;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ExcelComputerOutputWriter implements ComputerOutputWriter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelComputerOutputWriter.class);
    private static final String EXCEL_EXTENSION = ".xlsx";
    private static final String LAP_ENDING = "LAP#";
    private static final String PC_ENDING = "PC#";
    private static final String AGREEMENT = "Wyrazam zgode na przetwarzanie moich danych osobowych zgodnie z ustawa o ochronie danych osobowych w zwiazku z realizacja zlecenia. Podanie danych jest dobrowolne, ale niezbedne do przetworzenia zlecenia. Zostalem/am poinformowany/a, ze przysluguje mi prawo dostepu do swoich danych, mozliwosci ich poprawiania lub zadania zaprzestania ich przetwarzania.";
    private static final String CONFIRM_CLIENT_SIGN = "Potwierdzam odbior sprzetu\n oraz nie mam zadnych zastrzezen\n co do jego stanu - data i podpis:";
    private static final String NOT_TO_REPAIR = "Firma Kontrast nie ponosi odpowiedzielnosci za dane na dyskach twardych. Wydanie rewersu nie jest rownoznaczne z przyjeciem towaru do naprawy.";
    private static final String CLIENT_SIGN = "Podpis klienta";
    private static final String COMPANY_TERMS = "W przypadku braku karty gwarancyjnej lub brakujacego sprzetu za date przyjecia uwaza sie date dostarczenia brakow.\n Potwierdzenie zlecenia jest jedynym dokument uprawniajacym do odbioru sprzetu.\n Do czasu naprawy nie wlicza sie sobot i dni wolnych od pracy.\n Sprzet nieodebrany w ciagu trzech miesiecy przechodzi na wlasnosc serwisu celem pokrycia kosztow magazynowych.\n W razie dostarczenia sprzetu do modernizacji, serwis nie odpowiada za wady wykryte w czasie czynnosci serwisowych.\nW razie dostarczenia do serwisu sprawnego sprzetu reklamujacy ponosi koszty testow.\nSprzet zostanie zdiagnozowany tylko pod katem usterki podanej przez Klienta.\n W przypadku reinstalacji oprogramowania nalezy dostarczyc nosnik i numer licnecji.\n W przypadku realizacji za klienta serwisu gwarancji zewnetrznej firmie, Kontrast nie ponosi odpowiedzialnosci za czas realizacji.";
    private static final String FONT_TIMES_NEW_ROMAN = "Times New Roman";
    private final List<String> userHeaders = List.of("Imie", "Nazwisko", "Telefon", "Numer zlecenia", "Data przyjecia");
    private final List<String> laptopHeaders = List.of("Model", "Numer seryjny", "Inne", "Opis usterki", "Kasowac dane?");
    private final List<String> personalComputerHeaders = List.of("CPU", "MB", "RAM", "PSU", "Dysk", "Inne", "Opis usterki", "Kasowac dane?");
    DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm");
    Date dateobj = new Date();
    private long orderNumber = (System.currentTimeMillis() / 1000);

    @Override
    public Pair<String, byte[]> writeOutput(User user, Laptop laptop) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Order Details");

        userHeaders(workbook, sheet);
        userRow(user, workbook, sheet);
        laptopHeader(workbook, sheet);
        laptopRow(laptop, workbook, sheet);
        dataAgreementRow(workbook, sheet);
        clientAcceptSignRow(workbook, sheet);
        userLaptopConfirmationHeaders(workbook, sheet);
        userConfirmationRow(workbook, sheet);
        laptopConfirmationHeader(workbook, sheet);
        laptopConfirmationRow(laptop, workbook, sheet);
        dataAgreementConfirmationRow(workbook, sheet);
        companyTermsRow(workbook, sheet);

        sheet.setColumnWidth(0, 2000);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        sheet.autoSizeColumn(4);

        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            return Pair.of(LAP_ENDING + user.getLastName() + "_" + orderNumber + EXCEL_EXTENSION, bos.toByteArray());
        } catch (IOException e) {
            LOGGER.error("Error", e);
            return null;
        }
    }

    @Override
    public Pair<String, byte[]> writeOutput(User user, PersonalComputer personalComputer) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Order Details");

        userHeaders(workbook, sheet);
        userRow(user, workbook, sheet);
        personalComputerHeader(workbook, sheet);
        personalComputerRow(workbook, sheet, personalComputer);
        dataAgreementRow(workbook, sheet);
        clientAcceptSignRow(workbook, sheet);
        infoForClient(workbook, sheet, personalComputer);

        sheet.setColumnWidth(0, 2000);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        sheet.autoSizeColumn(4);
        sheet.autoSizeColumn(5);
        sheet.autoSizeColumn(6);
        sheet.autoSizeColumn(7);

        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            return Pair.of(PC_ENDING + user.getLastName() + "_" + orderNumber + EXCEL_EXTENSION, bos.toByteArray());
        } catch (IOException e) {
            LOGGER.error("Error", e);
            return null;
        }
    }

    private CellStyle userRowStyle(Workbook workbook) {
        CellStyle userRowStyle = workbook.createCellStyle();
        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName(FONT_TIMES_NEW_ROMAN);
        font.setFontHeightInPoints((short) 8);
        font.setBold(false);
        userRowStyle.setAlignment(HorizontalAlignment.LEFT);
        userRowStyle.setFont(font);
        userRowStyle.setBorderTop(BorderStyle.THIN);
        userRowStyle.setBorderBottom(BorderStyle.THIN);
        userRowStyle.setBorderRight(BorderStyle.THIN);
        userRowStyle.setBorderLeft(BorderStyle.THIN);
        return userRowStyle;
    }

    private CellStyle userHeaderStyle(Workbook workbook) {
        CellStyle userHeaderStyle = workbook.createCellStyle();
        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName(FONT_TIMES_NEW_ROMAN);
        font.setFontHeightInPoints((short) 9);
        font.setBold(true);
        userHeaderStyle.setAlignment(HorizontalAlignment.LEFT);
        userHeaderStyle.setFont(font);
        userHeaderStyle.setBorderRight(BorderStyle.THIN);
        userHeaderStyle.setBorderTop(BorderStyle.THIN);
        userHeaderStyle.setBorderBottom(BorderStyle.THIN);
        userHeaderStyle.setBorderLeft(BorderStyle.THIN);
        return userHeaderStyle;
    }

    private CellStyle dataAgreementStyle(Workbook workbook) {
        CellStyle dataAgreementStyle = workbook.createCellStyle();
        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName(FONT_TIMES_NEW_ROMAN);
        font.setFontHeightInPoints((short) 5);
        font.setBold(false);
        dataAgreementStyle.setWrapText(true);
        dataAgreementStyle.setAlignment(HorizontalAlignment.LEFT);
        dataAgreementStyle.setFont(font);
        dataAgreementStyle.setBorderLeft(BorderStyle.THIN);
        dataAgreementStyle.setBorderBottom(BorderStyle.THIN);
        dataAgreementStyle.setBorderTop(BorderStyle.THIN);
        dataAgreementStyle.setBorderRight(BorderStyle.THIN);
        return dataAgreementStyle;
    }

    private CellStyle orderClientSignStyle(Workbook workbook) {
        CellStyle orderClientStyle = workbook.createCellStyle();
        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName(FONT_TIMES_NEW_ROMAN);
        font.setFontHeightInPoints((short) 7);
        font.setBold(false);
        orderClientStyle.setWrapText(true);
        orderClientStyle.setAlignment(HorizontalAlignment.LEFT);
        orderClientStyle.setFont(font);
        orderClientStyle.setBorderLeft(BorderStyle.THIN);
        orderClientStyle.setBorderBottom(BorderStyle.THIN);
        orderClientStyle.setBorderRight(BorderStyle.THIN);
        orderClientStyle.setBorderTop(BorderStyle.THIN);
        return orderClientStyle;
    }

    private void userHeaders(Workbook workbook, Sheet sheet) {
        Row userHeader = sheet.createRow(0);
        for (int i = 0; i < userHeaders.size(); i++) {
            Cell cell = userHeader.createCell(i);
            cell.setCellValue(userHeaders.get(i));
            cell.setCellStyle(userHeaderStyle(workbook));
        }
    }

    private void userRow(User user, Workbook workbook, Sheet sheet) {
        Row userRow = sheet.createRow(1);
        Cell nameCell = userRow.createCell(0);
        nameCell.setCellValue(user.getName());
        nameCell.setCellStyle(userRowStyle(workbook));
        Cell lastNameCell = userRow.createCell(1);
        lastNameCell.setCellValue(user.getLastName());
        lastNameCell.setCellStyle(userRowStyle(workbook));
        lastNameCell.setCellStyle(userRowStyle(workbook));
        Cell phoneNumberCell = userRow.createCell(2);
        phoneNumberCell.setCellValue(user.getPhoneNumber());
        phoneNumberCell.setCellStyle(userRowStyle(workbook));
        Cell orderNumberCell = userRow.createCell(3);
        orderNumberCell.setCellValue(orderNumber);
        orderNumberCell.setCellStyle(userRowStyle(workbook));
        Cell dataFormatCell = userRow.createCell(4);
        dataFormatCell.setCellValue(df.format(dateobj));
        dataFormatCell.setCellStyle(userRowStyle(workbook));
    }

    private void laptopHeader(Workbook workbook, Sheet sheet) {
        Row laptopHeader = sheet.createRow(2);
        for (int i = 0; i < laptopHeaders.size(); i++) {
            Cell cell = laptopHeader.createCell(i);
            cell.setCellValue(laptopHeaders.get(i));
            cell.setCellStyle(userHeaderStyle(workbook));
        }
    }

    private void laptopConfirmationHeader(Workbook workbook, Sheet sheet) {
        Row laptopHeader = sheet.createRow(11);
        for (int i = 0; i < laptopHeaders.size(); i++) {
            Cell cell = laptopHeader.createCell(i);
            cell.setCellValue(laptopHeaders.get(i));
            cell.setCellStyle(userHeaderStyle(workbook));
        }
    }

    private void personalComputerHeader(Workbook workbook, Sheet sheet) {
        Row personalComputerHeader = sheet.createRow(2);
        for (int i = 0; i < personalComputerHeaders.size(); i++) {
            Cell cell = personalComputerHeader.createCell(i);
            cell.setCellValue(personalComputerHeaders.get(i));
            cell.setCellStyle(userHeaderStyle(workbook));
        }
    }

    private void laptopRow(Laptop laptop, Workbook workbook, Sheet sheet) {
        Row laptopRow = sheet.createRow(3);
        Cell modelCell = laptopRow.createCell(0);
        modelCell.setCellValue(laptop.getModel());
        modelCell.setCellStyle(userRowStyle(workbook));
        Cell serialNumberCell = laptopRow.createCell(1);
        serialNumberCell.setCellValue(laptop.getSerialNumber());
        serialNumberCell.setCellStyle(userRowStyle(workbook));
        Cell otherInfoCell = laptopRow.createCell(2);
        otherInfoCell.setCellValue(laptop.getOtherInfo());
        otherInfoCell.setCellStyle(userRowStyle(workbook));
        Cell problemDescriptionCell = laptopRow.createCell(3);
        problemDescriptionCell.setCellValue(laptop.getProblemDescription());
        problemDescriptionCell.setCellStyle(userRowStyle(workbook));
        Cell cleanDataCell = laptopRow.createCell(4);
        cleanDataCell.setCellValue(laptop.getCleanData());
        cleanDataCell.setCellStyle(userRowStyle(workbook));
    }

    private void laptopConfirmationRow(Laptop laptop, Workbook workbook, Sheet sheet) {
        Row laptopRow = sheet.createRow(12);
        Cell modelCell = laptopRow.createCell(0);
        modelCell.setCellValue(laptop.getModel());
        modelCell.setCellStyle(userRowStyle(workbook));
        Cell serialNumberCell = laptopRow.createCell(1);
        serialNumberCell.setCellValue(laptop.getSerialNumber());
        serialNumberCell.setCellStyle(userRowStyle(workbook));
        Cell otherInfoCell = laptopRow.createCell(2);
        otherInfoCell.setCellValue(laptop.getOtherInfo());
        otherInfoCell.setCellStyle(userRowStyle(workbook));
        Cell problemDescriptionCell = laptopRow.createCell(3);
        problemDescriptionCell.setCellValue(laptop.getProblemDescription());
        problemDescriptionCell.setCellStyle(userRowStyle(workbook));
        Cell cleanDataCell = laptopRow.createCell(4);
        cleanDataCell.setCellValue(laptop.getCleanData());
        cleanDataCell.setCellStyle(userRowStyle(workbook));
    }

    private void dataAgreementRow(Workbook workbook, Sheet sheet) {
        Row dataAgreementRow = sheet.createRow(sheet.getLastRowNum() + 1);
        CellRangeAddress range = (new CellRangeAddress(4, 4, 0, 3));
        sheet.addMergedRegion(range);
        RegionUtil.setBorderBottom(BorderStyle.THIN, range, sheet);
        RegionUtil.setBorderLeft(BorderStyle.THIN, range, sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, range, sheet);
        CellRangeAddress range1 = (new CellRangeAddress(4, 4, 4, 6));
        sheet.addMergedRegion(range1);
        RegionUtil.setBorderBottom(BorderStyle.THIN, range1, sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, range1, sheet);
        RegionUtil.setBorderTop(BorderStyle.THIN, range1, sheet);
        dataAgreementRow.setHeightInPoints(30);
        Cell dataAgreementCell = dataAgreementRow.createCell(0);
        dataAgreementCell.setCellValue(AGREEMENT);
        dataAgreementCell.setCellStyle(dataAgreementStyle(workbook));
        Cell signCell = dataAgreementRow.createCell(4);
        signCell.setCellValue(CLIENT_SIGN);
        signCell.setCellStyle(userRowStyle(workbook));
    }

    private void clientAcceptSignRow(Workbook workbook, Sheet sheet) {
        Row clientAcceptSignRow = sheet.createRow(sheet.getLastRowNum() + 1);
        CellRangeAddress range2 = (new CellRangeAddress(5, 5, 0, 4));
        sheet.addMergedRegion(range2);
        RegionUtil.setBorderBottom(BorderStyle.THIN, range2, sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, range2, sheet);
        clientAcceptSignRow.setHeightInPoints(35);
        Cell clientAcceptCell = clientAcceptSignRow.createCell(0);
        clientAcceptCell.setCellValue(CONFIRM_CLIENT_SIGN);
        clientAcceptCell.setCellStyle(orderClientSignStyle(workbook));
    }

    private void personalComputerRow(Workbook workbook, Sheet sheet, PersonalComputer personalComputer) {
        Row personalComputerRow = sheet.createRow(3);
        Cell cpuCell = personalComputerRow.createCell(0);
        cpuCell.setCellValue(personalComputer.getCpu());
        cpuCell.setCellStyle(userRowStyle(workbook));
        Cell motherBoardCell = personalComputerRow.createCell(1);
        motherBoardCell.setCellValue(personalComputer.getMotherBoard());
        motherBoardCell.setCellStyle(userRowStyle(workbook));
        Cell memoryRamCell = personalComputerRow.createCell(2);
        memoryRamCell.setCellValue(personalComputer.getMemoryRam());
        memoryRamCell.setCellStyle(userRowStyle(workbook));
        Cell psuCell = personalComputerRow.createCell(3);
        psuCell.setCellValue(personalComputer.getPsu());
        psuCell.setCellStyle(userRowStyle(workbook));
        Cell discCell = personalComputerRow.createCell(4);
        discCell.setCellValue(personalComputer.getDisc());
        discCell.setCellStyle(userRowStyle(workbook));
        Cell otherInfoCell = personalComputerRow.createCell(5);
        otherInfoCell.setCellValue(personalComputer.getOtherInfo());
        otherInfoCell.setCellStyle(userRowStyle(workbook));
        Cell problemDescriptionCell = personalComputerRow.createCell(6);
        problemDescriptionCell.setCellValue(personalComputer.getProblemDescription());
        problemDescriptionCell.setCellStyle(userRowStyle(workbook));
        Cell cleanDataCell = personalComputerRow.createCell(7);
        cleanDataCell.setCellValue(personalComputer.getCleanData());
        cleanDataCell.setCellStyle(userRowStyle(workbook));
    }

    private void userConfirmationHeaders(Workbook workbook, Sheet sheet) {
        Row userHeader = sheet.createRow(sheet.getLastRowNum() + 3);
        Cell orderNrCell = userHeader.createCell(3);
        orderNrCell.setCellValue(userHeaders.get(3));
        orderNrCell.setCellStyle(userHeaderStyle(workbook));
        Cell dataOrder = userHeader.createCell(4);
        dataOrder.setCellValue(userHeaders.get(4));
        dataOrder.setCellStyle(userHeaderStyle(workbook));
    }

    private void userLaptopConfirmationHeaders(Workbook workbook, Sheet sheet) {
        Row userHeader = sheet.createRow(sheet.getLastRowNum() + 4);
        Cell orderNrCell = userHeader.createCell(3);
        orderNrCell.setCellValue(userHeaders.get(3));
        orderNrCell.setCellStyle(userHeaderStyle(workbook));
        Cell dataOrder = userHeader.createCell(4);
        dataOrder.setCellValue(userHeaders.get(4));
        dataOrder.setCellStyle(userHeaderStyle(workbook));
    }

    private void userConfirmationRow(Workbook workbook, Sheet sheet) {
        Row userRow = sheet.createRow(sheet.getLastRowNum() + 1);

        Cell orderNumberCell = userRow.createCell(3);
        orderNumberCell.setCellValue(orderNumber);
        orderNumberCell.setCellStyle(userRowStyle(workbook));
        Cell dataFormatCell = userRow.createCell(4);
        dataFormatCell.setCellValue(df.format(dateobj));
        dataFormatCell.setCellStyle(userRowStyle(workbook));
    }

    private void infoForClient(Workbook workbook, Sheet sheet, PersonalComputer personalComputer) {
        Row infoClientRow = sheet.createRow(sheet.getLastRowNum() + 1);
        infoClientRow.createCell(0);
        userConfirmationHeaders(workbook, sheet);
        userConfirmationRow(workbook, sheet);
        personalComputerConfirmationHeader(workbook, sheet);
        personalComputerConfirmationRow(workbook, sheet, personalComputer);
        dataAgreementConfirmationRow(workbook, sheet);
        companyTermsRow(workbook, sheet);
    }

    private void personalComputerConfirmationHeader(Workbook workbook, Sheet sheet) {
        Row personalComputerHeader = sheet.createRow(sheet.getLastRowNum() + 1);
        for (int i = 0; i < personalComputerHeaders.size(); i++) {
            Cell cell = personalComputerHeader.createCell(i);
            cell.setCellValue(personalComputerHeaders.get(i));
            cell.setCellStyle(userHeaderStyle(workbook));
        }
    }

    private void personalComputerConfirmationRow(Workbook workbook, Sheet sheet, PersonalComputer personalComputer) {
        Row personalComputerInfoRow = sheet.createRow(sheet.getLastRowNum() + 1);
        Cell cpuCell = personalComputerInfoRow.createCell(0);
        cpuCell.setCellValue(personalComputer.getCpu());
        cpuCell.setCellStyle(userRowStyle(workbook));
        Cell motherBoardCell = personalComputerInfoRow.createCell(1);
        motherBoardCell.setCellValue(personalComputer.getMotherBoard());
        motherBoardCell.setCellStyle(userRowStyle(workbook));
        Cell memoryRamCell = personalComputerInfoRow.createCell(2);
        memoryRamCell.setCellValue(personalComputer.getMemoryRam());
        memoryRamCell.setCellStyle(userRowStyle(workbook));
        Cell psuCell = personalComputerInfoRow.createCell(3);
        psuCell.setCellValue(personalComputer.getPsu());
        psuCell.setCellStyle(userRowStyle(workbook));
        Cell discCell = personalComputerInfoRow.createCell(4);
        discCell.setCellValue(personalComputer.getDisc());
        discCell.setCellStyle(userRowStyle(workbook));
        Cell otherInfoCell = personalComputerInfoRow.createCell(5);
        otherInfoCell.setCellValue(personalComputer.getOtherInfo());
        otherInfoCell.setCellStyle(userRowStyle(workbook));
        Cell problemDescriptionCell = personalComputerInfoRow.createCell(6);
        problemDescriptionCell.setCellValue(personalComputer.getProblemDescription());
        problemDescriptionCell.setCellStyle(userRowStyle(workbook));
        Cell cleanDataCell = personalComputerInfoRow.createCell(7);
        cleanDataCell.setCellValue(personalComputer.getCleanData());
        cleanDataCell.setCellStyle(userRowStyle(workbook));
    }

    private void dataAgreementConfirmationRow(Workbook workbook, Sheet sheet) {
        Row dataAgreementRow = sheet.createRow(13);
        CellRangeAddress range1 = (new CellRangeAddress(13, 13, 0, 3));
        sheet.addMergedRegion(range1);
        RegionUtil.setBorderBottom(BorderStyle.THIN, range1, sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, range1, sheet);
        RegionUtil.setBorderTop(BorderStyle.THIN, range1, sheet);
        dataAgreementRow.setHeightInPoints(30);
        Cell dataAgreementCell = dataAgreementRow.createCell(0);
        dataAgreementCell.setCellValue(NOT_TO_REPAIR);
        dataAgreementCell.setCellStyle(dataAgreementStyle(workbook));
        CellRangeAddress range199 = (new CellRangeAddress(13, 13, 4, 6));
        sheet.addMergedRegion(range199);
        RegionUtil.setBorderBottom(BorderStyle.THIN, range199, sheet);
        RegionUtil.setBorderLeft(BorderStyle.THIN, range199, sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, range199, sheet);
        RegionUtil.setBorderTop(BorderStyle.THIN, range199, sheet);
        dataAgreementRow.setHeightInPoints(30);
        Cell signCell = dataAgreementRow.createCell(4);
        signCell.setCellValue(CLIENT_SIGN);
        signCell.setCellStyle(userRowStyle(workbook));
    }

    private void companyTermsRow(Workbook workbook, Sheet sheet) {
        Row companyTermsConfRow = sheet.createRow(14);
        CellRangeAddress range9 = (new CellRangeAddress(14, 14, 0, 4));
        sheet.addMergedRegion(range9);
        RegionUtil.setBorderBottom(BorderStyle.THIN, range9, sheet);
        RegionUtil.setBorderLeft(BorderStyle.THIN, range9, sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, range9, sheet);
        RegionUtil.setBorderTop(BorderStyle.THIN, range9, sheet);
        companyTermsConfRow.setHeightInPoints(70);
        Cell companyTermsCell = companyTermsConfRow.createCell(0);
        companyTermsCell.setCellValue(COMPANY_TERMS);
        companyTermsCell.setCellStyle(dataAgreementStyle(workbook));
    }
}

