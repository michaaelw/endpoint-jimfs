package com.endponts.jimfs;

import io.vavr.control.Either;

import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.lang.String.join;
import static java.util.stream.Collectors.toList;

/**
 * Represents a files system and performs file system operations.
 * @author mw
 * @version 0.9
 */
public class FileSystem {

    /**
     * The FileSystem root directory.
     */
    private final Directory root;
    /**
     * The File System item name invalid characters.
     */
    private final String[] DIR_INVALID_CHARS = {" ", ":"};
    /**
     * Construct a new FileSystem with root directory.
     */
    public FileSystem() {
        root = new Directory("", null);
    }

    /**
     * List all descendants of the FileSystem root.
     * @return a formatted string of the file system items
     */
    public String ls() {
        if (root.getDescendants().isEmpty()) {
            return "";
        }

        final TreeSet<FileSystemItem> contents = root.getDescendants();
        return buildPrintString(contents, 0);
    }

    /**
     * Builds a formatted LIST descendents output string recursively.
     * @param descendants set from which to build a portion of the output.
     * @param level the level at which the descendents sit.
     * @return the formatted list of descendents
     */
    protected String buildPrintString(final TreeSet<FileSystemItem> descendants,
                                      final int level) {
        final StringBuilder sb = new StringBuilder();
        final String levelIndentation = getLevelIndentation(level, "  ");
        for (FileSystemItem content : descendants) {
            sb.append(levelIndentation).append(content.getName()).append("\n");
            if (content.isDirectory()) {
                final TreeSet<FileSystemItem> children;
                children = ((Directory) content).getDescendants();
                final int incLevel = level + 1;
                final String printString = buildPrintString(children, incLevel);
                sb.append(printString);
            }
        }
        return sb.toString();
    }

    protected final String getLevelIndentation(final int level,
                                               final String value) {
        final StringBuilder indent = new StringBuilder();
        for (int i = 0; i < level; i++) {
            indent.append(value);
        }

        return indent.toString();
    }

    /**
     * Creates a new directory in the location specified. Does nothing if the
     * directory already exist.
     * @param path fof the directory.
     * @return either and error if something foes wrong or a boolean
     * confirming the creating was successful.
     */
    public final Either<Exception, Boolean> mkdir(final String path) {
        final String[] dirNames = path.split("/");
        final String[] invDirNames = getInvalidDirectoryNames(dirNames);

        // has invalid directory
        if (invDirNames.length > 0 || path.isEmpty()) {
            final String joinInvDirNames;
            joinInvDirNames = path.isEmpty() ? "" : join(" ", invDirNames);
            String fMsg = "Invalid directory name(s): [%s]";
            final String message = format(fMsg, joinInvDirNames);
            return Either.left(new IllegalArgumentException(message));
        }

        // is valid directory name
        Directory prevDirectory = root;
        for (final String dirName : dirNames) {
            Directory foundDir = (Directory) prevDirectory.findChild(dirName);
            if (foundDir == null) {
                final Directory newDirectory;
                newDirectory = new Directory(dirName, prevDirectory);
                prevDirectory.addChildItem(newDirectory);
                prevDirectory = newDirectory;
            } else {
                prevDirectory = foundDir;
            }
        }

        return Either.right(true);
    }

    /**
     * Move a directory from one path to another.
     * @param srcPath of the directory that will be moved
     * @param destPath of the directory that will receive the source directory
     * @return either an exception or a boolean indicate a successful move
     */
    public final Either<Exception, Boolean> mv(final String srcPath,
                                               final String destPath) {
        if (srcPath.equals(destPath)) {
            final String msgFmt = "Cannot move [%s] to [%s] - source and destination is the same";
            final String msg = format(msgFmt, srcPath, destPath);
            return Either.left(new IllegalArgumentException(msg));
        }

        final String[] srcDirNames = srcPath.split("/");
        if (srcDirNames.length <= 0) {
            final String msgFmt = "Cannot move [%s] to [%s] - no source directory provided";
            final String msg = format(msgFmt, srcPath, destPath);
            return Either.left(new IllegalArgumentException(msg));
        }

        final String[] dstDirNames = destPath.split("/");
        if (dstDirNames.length <= 0) {
            final String msgFmt = "Cannot move [%s] to [%s] - no destination directory provided";
            final String msg = format(msgFmt, srcPath, destPath);
            return Either.left(new IllegalArgumentException(msg));
        }

        final Either<Exception, Directory> srcDirNodeEither;
        srcDirNodeEither = findDirectory(srcDirNames);
        if (srcDirNodeEither.isLeft()) {
            return createFindDirError(srcPath, destPath, srcDirNodeEither);
        }

        final Either<Exception, Directory> destDirNodeEither;
        destDirNodeEither = findDirectory(dstDirNames);
        if (destDirNodeEither.isLeft()) {
            return createFindDirError(srcPath, destPath, destDirNodeEither);
        }

        Directory srcDir = srcDirNodeEither.get();
        srcDir.getParent().deleteItem(srcDir);

        Directory destDir = destDirNodeEither.get();
        destDir.addChildItem(srcDir);

        return Either.right(true);
    }

    private Either<Exception, Boolean> createFindDirError(
            final String srcPath,
            final String destPath,
            final Either<Exception, Directory> destDirNodeEither) {
        final String findErrMsg = destDirNodeEither.left().get().getMessage();
        final String msgFmt = "Cannot move [%s] to [%s] - %s";
        final String msg = format(msgFmt, srcPath, destPath, findErrMsg);
        return Either.left(new IllegalArgumentException(msg));
    }

    /**
     * Remove/Delete a directory.
     * @param path of the directory to delete. In a multi node path the
     *             last directory is removed.
     * @return either an exception or a boolean indication successful
     * deletion.
     */
    public final Either<Exception, Boolean> rm(final String path) {
        final String[] dirNames = path.split("/");

        if (dirNames.length <= 0) {
            final String msg = "Directory name required.";
            final IllegalArgumentException exception;
            exception = new IllegalArgumentException(msg);
            return Either.left(exception);
        }


        final Either<Exception, Directory> dirNodeEither;
        dirNodeEither = findDirectory(dirNames);
        if (dirNodeEither.isLeft()) {
            final String findErrorMessage;
            findErrorMessage = dirNodeEither.left().get().getMessage();
            final String msg = "Cannot delete %s - %s";
            final String message = format(msg, path, findErrorMessage);
            return Either.left(new IllegalArgumentException(message));
        }

        final Directory directory = dirNodeEither.right().get();
        directory.getParent().deleteItem(directory);

        return Either.right(true);
    }

    protected final Either<Exception, Directory> findDirectory(
            final String[] dirNames) {
        Directory current = root;
        for (String dirName : dirNames) {
            final Directory child = (Directory) current.findChild(dirName);
            if (child == null) {
                final String message = format("%s does not exist", dirName);
                return Either.left(new IllegalArgumentException(message));
            }
            current = child;
        }
        return Either.right(current);
    }

    protected final String[] getInvalidDirectoryNames(final String[] dns) {
        return Arrays.stream(dns)
                .filter(dn -> hasInvalidCharacters(dn, DIR_INVALID_CHARS))
                .toArray(String[]::new);
    }

    protected final boolean hasInvalidCharacters(final String dn,
                                                 final String ... s) {
        return !hasNoInvalidCharacters(dn, s);
    }

    protected final boolean hasNoInvalidCharacters(final String dn,
                                                   final String ... s) {
        final Stream<String> stream = Arrays.stream(s);
        final Stream<String> stringStream = stream.filter(dn::contains);
        final List<String> strings = stringStream.collect(toList());
        return strings.isEmpty();
    }

    protected final String[] getDirInvalidChars() {
        return DIR_INVALID_CHARS;
    }

}
