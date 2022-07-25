package com.endponts.jimfs;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestFileSystem {

    @Test
    public void hasInvalidCharacters() {
        // given
        final String input = "fruits";

        // when
        final FileSystem fileSystem = new FileSystem();
        String[] dirInvalidChars = fileSystem.getDirInvalidChars();
        final boolean invalidDirectoryName = fileSystem.hasInvalidCharacters(input, dirInvalidChars);

        // then
        boolean hasInvalidChars = false;
        assertEquals(invalidDirectoryName, hasInvalidChars);
    }

    @Test
    public void testGetInvalidDirectoryNames() {
        // given
        final String[] input = {"fruits"};

        // when
        final FileSystem fileSystem = new FileSystem();
        final String[] invalidDirectoryNames = fileSystem.getInvalidDirectoryNames(input);


        // then
        int emptyActual = 0;
        assertEquals(invalidDirectoryNames.length, emptyActual);
    }
}
