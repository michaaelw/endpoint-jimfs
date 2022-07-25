package com.endponts.jimfs;

import io.vavr.control.Either;

import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * A Real, Eval, Print Loop (REPL) for Endpoint Backend Coding Challenge.
 * @author mw
 * @version 0.9
 */
@SuppressWarnings("InfiniteLoopStatement")
public final class FileSystemRepl {

    private FileSystemRepl() {
    }

    /**
     * The entry point for the REPL.
     * @param args from the command line.
     */
    public static void main(final String[] args) {
        final Scanner scanner = new Scanner(new InputStreamReader(System.in));
        final FileSystem fileSystem = new FileSystem();
        boolean doNotQuit = true;
        System.out.println("Welcome to Endpoint Backend Coding Challenge ");
        do {
            try {
                System.out.print("> ");
                String input = scanner.nextLine();
                String evalRes = eval(input, fileSystem);
                System.out.println(evalRes);
            } catch (Exception e) {
                final String message = e.getMessage();
                if (message.equalsIgnoreCase("QUIT")) {
                    doNotQuit = false;
                }
                System.out.println("bye.");
            }
        } while (doNotQuit);
    }

    protected static String eval(final String input,
                                 final FileSystem fileSystem) {
        final CommandData commandData = getCommandData(input);
        final String upperCaseCommand = commandData.command.toUpperCase();
        final String value = commandData.value;

        switch (upperCaseCommand) {
            case "CREATE":
                final Either<Exception, Boolean> mkdirRes;
                mkdirRes = fileSystem.mkdir(value);

                if (mkdirRes.isLeft())
                    return mkdirRes.left().get().getMessage();

                return String.format("CREATE %s", value);
            case "DELETE":
                final Either<Exception, Boolean> rmRes;
                rmRes = fileSystem.rm(value);

                if (rmRes.isLeft())
                    return rmRes.left().get().getMessage();

                return String.format("DELETE %s", value);
            case "MOVE":
                final SrcDestData srcDestData = getSrcDestData(value);
                final Either<Exception, Boolean> mvRes;
                mvRes = fileSystem.mv(srcDestData.src, srcDestData.dest);

                if (mvRes.isLeft())
                    return mvRes.left().get().getMessage();

                return String.format("MOVE %s", value);
            case "LIST":
                final String ls = fileSystem.ls();
                final StringBuilder sb = new StringBuilder();
                final String list;
                list = sb.append("LIST").append("\n").append(ls).toString();
                return (ls.isEmpty()) ? "LIST" : list;
            case "HELP":
                return getHelpInfo();
            case "QUIT", "EXIT":
                throw new RuntimeException("QUIT");
            default:
                return String.format("Unknown command: %s", upperCaseCommand);
        }
    }

    private static String getHelpInfo() {
        return """
                Commands:
                CREATE          creates a directory. (CREATE dir)
                DELETE          deletes a directory. (DELETE dir)
                LIST            list all existing directories.
                MOVE            move a directory (MOVE src dest)
                """;
    }

    protected static CommandData getCommandData(final String input) {
        final String trimInput = input.strip();
        final String[] s = trimInput.split(" ", 2);
        final CommandData commandData = new CommandData();

        if (s.length <= 0) {
            commandData.command = "";
            commandData.value = "";
            return commandData;
        } else if (s.length == 1) {
            commandData.command = s[0].strip();
            commandData.value = "";
            return commandData;
        } else if (s.length == 2) {
            commandData.command = s[0];
            commandData.value = s[1];
            return commandData;
        }

        return commandData;
    }

    protected static SrcDestData getSrcDestData(final String value) {
        final String trimInput = value.strip();
        final String[] s = trimInput.split(" ");
        final SrcDestData srcDestData = new SrcDestData();

        if (s.length <= 0) {
            srcDestData.src = "";
            srcDestData.dest = "";
        } else if (s.length == 1) {
            srcDestData.src = s[0];
            srcDestData.dest = "";
        } else {
            srcDestData.src = s[0];
            srcDestData.dest = s[1];
        }

        return srcDestData;
    }

    /**
     * Holder of the command data.
     */
    static class CommandData {
        /**
         * The command.
         */
        String command;
        /**
         * The command value.
         */
        String value;
    }

    /**
     * Holder of source and destination data.
     */
    static class SrcDestData {
        /**
         * The source.
         */
        String src;
        /**
         * The destination.
         */
        String dest;
    }
}
