package src.views;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public abstract class BaseMenuView {

    private static final String ERROR_PREFIX = "Erreur : ";
    private static final String LABEL_WITH_DEFAULT_FORMAT = "%s [%s] : ";

    protected final Scanner scanner;
    protected final Logger logger;

    protected BaseMenuView(Scanner scanner, Logger logger) {
        this.scanner = scanner;
        this.logger = logger;
        configureLogger(logger);
    }

    protected void logSection(String title) {
        logger.log(Level.INFO, "\n{0}", title);
    }

    protected void logInfo(String message) {
        logger.info(message);
    }

    protected void logError(String message) {
        logger.log(Level.WARNING, "{0}{1}", new Object[]{ERROR_PREFIX, message});
    }

    protected String prompt(String message) {
        logger.info(message);
        return scanner.nextLine();
    }

    protected String promptWithDefault(String label, String current) {
        return prompt(String.format(LABEL_WITH_DEFAULT_FORMAT, label, current));
    }

    protected int promptInt(String message) {
        while (true) {
            try {
                return Integer.parseInt(prompt(message).trim());
            } catch (NumberFormatException e) {
                logInfo("Veuillez saisir un nombre valide.");
            }
        }
    }

    protected double promptDouble(String message) {
        while (true) {
            try {
                return Double.parseDouble(prompt(message).trim());
            } catch (NumberFormatException e) {
                logInfo("Veuillez saisir un nombre valide.");
            }
        }
    }

    protected double promptDoubleWithDefault(String label, double current) {
        while (true) {
            String value = prompt(String.format("%s [%.2f] : ", label, current)).trim();
            if (value.isEmpty()) {
                return current;
            }
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                logInfo("Valeur invalide.");
            }
        }
    }

    protected boolean promptBoolean(String message) {
        while (true) {
            String input = prompt(message).trim().toLowerCase();
            if (input.equals("o") || input.equals("oui")) {
                return true;
            }
            if (input.equals("n") || input.equals("non")) {
                return false;
            }
            logInfo("Répondez par o/n.");
        }
    }

    protected boolean promptBooleanWithDefault(String label, boolean current) {
        while (true) {
            String input = prompt(String.format(LABEL_WITH_DEFAULT_FORMAT, label, current ? "oui" : "non")).trim().toLowerCase();
            if (input.isEmpty()) {
                return current;
            }
            if (input.equals("o") || input.equals("oui")) {
                return true;
            }
            if (input.equals("n") || input.equals("non")) {
                return false;
            }
            logInfo("Répondez par o/n.");
        }
    }

    protected LocalDate promptDate(String message) {
        while (true) {
            String value = prompt(message).trim();
            try {
                return LocalDate.parse(value);
            } catch (DateTimeParseException e) {
                logInfo("Format de date invalide.");
            }
        }
    }

    protected LocalDate promptDateOptional(String message) {
        String value = prompt(message).trim();
        if (value.isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException e) {
            logInfo("Format invalide. Date actuelle utilisée.");
            return null;
        }
    }

    protected LocalDate promptDateOptionalWithDefault(String label, LocalDate current) {
        while (true) {
            String value = prompt(String.format(LABEL_WITH_DEFAULT_FORMAT, label, current)).trim();
            if (value.isEmpty()) {
                return current;
            }
            try {
                return LocalDate.parse(value);
            } catch (DateTimeParseException e) {
                logInfo("Format invalide.");
            }
        }
    }

    private static void configureLogger(Logger logger) {
        logger.setUseParentHandlers(false);
        if (logger.getHandlers().length == 0) {
            ConsoleHandler handler = new ConsoleHandler();
            handler.setFormatter(PLAIN_FORMATTER);
            handler.setLevel(Level.ALL);
            logger.addHandler(handler);
        } else {
            for (java.util.logging.Handler handler : logger.getHandlers()) {
                handler.setFormatter(PLAIN_FORMATTER);
                handler.setLevel(Level.ALL);
            }
        }
        logger.setLevel(Level.ALL);
    }

    private static final Formatter PLAIN_FORMATTER = new Formatter() {
        @Override
        public String format(LogRecord logRecord) {
            String message = formatMessage(logRecord);
            return message + System.lineSeparator();
        }
    };
}
