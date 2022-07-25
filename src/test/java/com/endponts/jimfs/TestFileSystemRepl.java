package com.endponts.jimfs;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class TestFileSystemRepl {

    @Test
    public void testEvalWithEmptyInput() {
        // given an empty string input
        final String input = "";

        // when
        final FileSystem fs = new FileSystem();
        final String result = FileSystemRepl.eval(input, fs);

        // then
        Assertions.assertEquals("Unknown command: ", result);
    }

    @Test
    public void testEvalWithCreateCommandAndNoValueInput() {
        // given an empty string input
        String input = "CREATE";

        // when
        final FileSystem fs = new FileSystem();
        final String result = FileSystemRepl.eval(input, fs);

        // then
        Assertions.assertEquals("Invalid directory name(s): []", result);
    }

    @Test
    public void testEvalWithCreateCommandAndValueFruitsWithSpaceInput() {
        // given an empty string input
        final String input = "CREATE fru its";

        // when
        final FileSystem fs = new FileSystem();
        final String result = FileSystemRepl.eval(input, fs);

        // then
        Assertions.assertEquals("Invalid directory name(s): [fru its]", result);
    }

    @Test
    public void testEvalWithCreateCommandAndValueFruitsInput() {
        // given an empty string input
        final String input = "CREATE fruits";

        // when
        final FileSystem fs = new FileSystem();
        final String result = FileSystemRepl.eval(input, fs);

        // then
        Assertions.assertEquals(input, result);
    }

    @Test
    public void testEvalWithCreateCommandAndDuplicateValueFruitsInput() {
        // given an empty string input
        final String createFruitsFirst = "CREATE fruits";
        final String createFruitsSecond = "CREATE fruits";
        final String firstList = "LIST";

        // when
        final FileSystem fs = new FileSystem();
        final String createFruitsFirstRes = FileSystemRepl.eval(createFruitsFirst, fs);
        final String createFruitsSecondRes = FileSystemRepl.eval(createFruitsSecond, fs);
        final String firstListRes = FileSystemRepl.eval(firstList, fs);

        // then
        Assertions.assertEquals(createFruitsFirst, createFruitsFirstRes);
        Assertions.assertEquals(createFruitsSecond, createFruitsSecondRes);
        final String expectFirstList = """
                LIST
                fruits
                """;
        Assertions.assertEquals(expectFirstList, firstListRes);
    }

    @Test
    public void testEvalWithCreateCommandWithWhiteSpaceLeftAndValueFruitsInput() {
        // given an empty string input
        final String inputData = "CREATE fruits";
        final String input = String.format("   %s", inputData);

        // when
        final FileSystem fs = new FileSystem();
        final String result = FileSystemRepl.eval(input, fs);

        // then
        Assertions.assertEquals(inputData, result);
    }

    @Test
    public void testEvalWithCreateCommandWithWhiteSpaceRightAndValueFruitsInput() {
        // given an empty string input
        final String inputData = "CREATE fruits";
        final String input = String.format("%s   ", inputData);

        // when
        final FileSystem fs = new FileSystem();
        final String result = FileSystemRepl.eval(input, fs);

        // then
        Assertions.assertEquals(inputData, result);
    }

    @Test
    public void testEvalWithCreateCommandAndValueFruitsAndApplesAndFujiGrainsInput() {
        // given an empty string input
        final String input = "CREATE fruits/apples/fuji";

        // when
        final FileSystem fs = new FileSystem();
        final String result = FileSystemRepl.eval(input, fs);

        // then
        Assertions.assertEquals(input, result);
    }

    @Test
    public void testEvalWithListCommandWithNoValueInput() {
        // given an empty string input
        final String input = "LIST";

        // when
        final FileSystem fs = new FileSystem();
        final String result = FileSystemRepl.eval(input, fs);

        // then
        Assertions.assertEquals(input, result);
    }

    @Test
    public void testEvalWithMoveCommandAndValueToPathInput() {
        // given an empty string input
        final String createFruitsApples = "CREATE fruits/apples";
        final String createFruitsGrains = "CREATE fruits/grains";
        final String moveFruitsApplesToFruitsGrains = "MOVE fruits/apples fruits/grains";
        final String firstList = "LIST";

        // when
        final FileSystem fs = new FileSystem();
        final String createFruitsApplesFujiRes = FileSystemRepl.eval(createFruitsApples, fs);
        final String createFruitsGrainsBreadRes = FileSystemRepl.eval(createFruitsGrains, fs);
        final String beforeMoveListRes = FileSystemRepl.eval(firstList, fs);
        final String moveFruitsApplesToFruitsGrainsRes = FileSystemRepl.eval(moveFruitsApplesToFruitsGrains, fs);
        final String afterMoveListRes = FileSystemRepl.eval(firstList, fs);

        // then
        Assertions.assertEquals(createFruitsApples, createFruitsApplesFujiRes);
        Assertions.assertEquals(createFruitsGrains, createFruitsGrainsBreadRes);
        Assertions.assertEquals(moveFruitsApplesToFruitsGrains, moveFruitsApplesToFruitsGrainsRes);
        final String beforeMoveListExpected = """
                LIST
                fruits  
                  apples                                
                  grains
                """;
        Assertions.assertEquals(beforeMoveListExpected, beforeMoveListRes);
        final String afterMoveListExpected = """
                LIST
                fruits                  
                  grains
                    apples
                """;
        Assertions.assertEquals(afterMoveListExpected, afterMoveListRes);
    }

    @Test
    public void testEvalWithDeleteCommandAndNoParentDirInput() {
        // given an empty string input
        final String deleteGrains = "DELETE fruits/grains";

        // when
        final FileSystem fs = new FileSystem();
        final String deleteGrainsRes = FileSystemRepl.eval(deleteGrains, fs);

        // then
        final String expected = "Cannot delete fruits/grains - fruits does not exist";
        Assertions.assertEquals(expected, deleteGrainsRes);
    }

    @Test
    public void testEvalWithDeleteCommandAndNoChildDirInput() {
        // given an empty string input
        final String createFruits = "CREATE fruits";
        final String deleteGrains = "DELETE fruits/grains";

        // when
        final FileSystem fs = new FileSystem();
        final String createGrainsRes = FileSystemRepl.eval(createFruits, fs);
        final String deleteGrainsRes = FileSystemRepl.eval(deleteGrains, fs);

        // then
        Assertions.assertEquals(createFruits, createGrainsRes);
        final String expected = "Cannot delete fruits/grains - grains does not exist";
        Assertions.assertEquals(expected, deleteGrainsRes);
    }

    @Test
    public void testEvalWithDeleteCommandAndValueToPathInput() {
        // given an empty string input
        final String createFruitsGrainsBread = "CREATE fruits/grains/bread";
        final String firstList = "LIST";
        final String deleteBread = "DELETE fruits/grains/bread";
        final String secondList = "LIST";
        final String deleteGrains = "DELETE fruits/grains";
        final String thirdList = "LIST";

        // when
        final FileSystem fs = new FileSystem();
        final String createFruitsGrainsBreadRes = FileSystemRepl.eval(createFruitsGrainsBread, fs);
        final String firstListRes = FileSystemRepl.eval(firstList, fs);
        final String deleteBreadRes = FileSystemRepl.eval(deleteBread, fs);
        final String secondListRes = FileSystemRepl.eval(secondList, fs);
        final String deleteGrainsRes = FileSystemRepl.eval(deleteGrains, fs);
        final String thirdListRes = FileSystemRepl.eval(thirdList, fs);

        // then
        Assertions.assertEquals(createFruitsGrainsBread, createFruitsGrainsBreadRes);
        final String firstListExpected = """
                LIST
                fruits  
                  grains
                    bread
                """;
        Assertions.assertEquals(firstListExpected, firstListRes);
        Assertions.assertEquals(deleteBread, deleteBreadRes);
        final String secondListExpected = """
                LIST
                fruits  
                  grains
                """;
        Assertions.assertEquals(secondListExpected, secondListRes);
        Assertions.assertEquals(deleteGrains, deleteGrainsRes);
        final String thirdListExpected = """
                LIST
                fruits  
                """;
        Assertions.assertEquals(thirdListExpected, thirdListRes);
    }

    @Test
    public void testEvalCreateCommandMultiWithListInput() {
        // given
        final String createFruits = "CREATE fruits";
        final String createVegetables = "CREATE vegetables";
        final String createGrains = "CREATE grains";
        final String createApples = "CREATE fruits/apples";
        final String createFuji = "CREATE fruits/apples/fuji";
        final String list = "LIST";

        // when
        final FileSystem fs = new FileSystem();
        final String createFruitsRes = FileSystemRepl.eval(createFruits, fs);
        final String createVegetablesRes = FileSystemRepl.eval(createVegetables, fs);
        final String createGrainsRes = FileSystemRepl.eval(createGrains, fs);
        final String createApplesRes = FileSystemRepl.eval(createApples, fs);
        final String createFujiRes = FileSystemRepl.eval(createFuji, fs);
        final String listRes = FileSystemRepl.eval(list, fs);

        // then
        String listExpected = """
                LIST
                fruits
                  apples
                    fuji
                grains
                vegetables
                """;
        Assertions.assertEquals(createFruits, createFruitsRes);
        Assertions.assertEquals(createVegetables, createVegetablesRes);
        Assertions.assertEquals(createGrains, createGrainsRes);
        Assertions.assertEquals(createApples, createApplesRes);
        Assertions.assertEquals(createFuji, createFujiRes);
        Assertions.assertEquals(listExpected, listRes);
    }

    @Test
    public void testEvalCreateCommandMultiWithMoveInListInput() {
        // given
        final String createFruits = "CREATE fruits";
        final String createVegetables = "CREATE vegetables";
        final String createGrains = "CREATE grains";
        final String createApples = "CREATE fruits/apples";
        final String createFuji = "CREATE fruits/apples/fuji";
        final String list = "LIST";

        final String createSquash = "CREATE grains/squash";
        final String moveSquashVegetables = "MOVE grains/squash vegetables";
        final String createFoods = "CREATE foods";
        final String moveGrainsFoods = "MOVE grains foods";
        final String moveFruitsFoods = "MOVE fruits foods";
        final String createVegetablesFoods = "MOVE vegetables foods";

        // when
        final FileSystem fs = new FileSystem();
        final String createFruitsRes = FileSystemRepl.eval(createFruits, fs);
        final String createVegetablesRes = FileSystemRepl.eval(createVegetables, fs);
        final String createGrainsRes = FileSystemRepl.eval(createGrains, fs);
        final String createApplesRes = FileSystemRepl.eval(createApples, fs);
        final String createFujiRes = FileSystemRepl.eval(createFuji, fs);
        final String firstListRes = FileSystemRepl.eval(list, fs);

        final String createSquashRes = FileSystemRepl.eval(createSquash, fs);
        final String moveSquashVegetablesRes = FileSystemRepl.eval(moveSquashVegetables, fs);
        final String createFoodsRes = FileSystemRepl.eval(createFoods, fs);
        final String moveGrainsFoodsRes = FileSystemRepl.eval(moveGrainsFoods, fs);
        final String moveFruitsFoodsRes = FileSystemRepl.eval(moveFruitsFoods, fs);
        final String createVegetablesFoodsRes = FileSystemRepl.eval(createVegetablesFoods, fs);
        final String secondListRes = FileSystemRepl.eval(list, fs);

        // then
        Assertions.assertEquals(createFruits, createFruitsRes);
        Assertions.assertEquals(createVegetables, createVegetablesRes);
        Assertions.assertEquals(createGrains, createGrainsRes);
        Assertions.assertEquals(createApples, createApplesRes);
        Assertions.assertEquals(createFuji, createFujiRes);
        String firstListExpected = """
                LIST
                fruits
                  apples
                    fuji
                grains
                vegetables
                """;
        Assertions.assertEquals(firstListExpected, firstListRes);

        Assertions.assertEquals(createSquash, createSquashRes);
        Assertions.assertEquals(moveSquashVegetables, moveSquashVegetablesRes);
        Assertions.assertEquals(createFoods, createFoodsRes);
        Assertions.assertEquals(moveGrainsFoods, moveGrainsFoodsRes);
        Assertions.assertEquals(moveFruitsFoods, moveFruitsFoodsRes);
        Assertions.assertEquals(createVegetablesFoods, createVegetablesFoodsRes);
        String secondListExpected = """
                LIST
                foods
                  fruits
                    apples
                      fuji
                  grains
                  vegetables
                    squash
                """;
        Assertions.assertEquals(secondListExpected, secondListRes);
    }

    @Test
    public void testEvalCreateCommandMultiWithDeleteInListInput() {
        // given
        final String createFruits = "CREATE fruits";
        final String createVegetables = "CREATE vegetables";
        final String createGrains = "CREATE grains";
        final String createApples = "CREATE fruits/apples";
        final String createFuji = "CREATE fruits/apples/fuji";
        final String list = "LIST";

        final String createSquash = "CREATE grains/squash";
        final String moveSquashVegetables = "MOVE grains/squash vegetables";
        final String createFoods = "CREATE foods";
        final String moveGrainsFoods = "MOVE grains foods";
        final String moveFruitsFoods = "MOVE fruits foods";
        final String createVegetablesFoods = "MOVE vegetables foods";

        final String deleteFruitsApples = "DELETE fruits/apples";
        final String deleteFoodsFruitsApples = "DELETE foods/fruits/apples";

        // when
        final FileSystem fs = new FileSystem();
        final String createFruitsRes = FileSystemRepl.eval(createFruits, fs);
        final String createVegetablesRes = FileSystemRepl.eval(createVegetables, fs);
        final String createGrainsRes = FileSystemRepl.eval(createGrains, fs);
        final String createApplesRes = FileSystemRepl.eval(createApples, fs);
        final String createFujiRes = FileSystemRepl.eval(createFuji, fs);
        final String firstListRes = FileSystemRepl.eval(list, fs);

        final String createSquashRes = FileSystemRepl.eval(createSquash, fs);
        final String moveSquashVegetablesRes = FileSystemRepl.eval(moveSquashVegetables, fs);
        final String createFoodsRes = FileSystemRepl.eval(createFoods, fs);
        final String moveGrainsFoodsRes = FileSystemRepl.eval(moveGrainsFoods, fs);
        final String moveFruitsFoodsRes = FileSystemRepl.eval(moveFruitsFoods, fs);
        final String createVegetablesFoodsRes = FileSystemRepl.eval(createVegetablesFoods, fs);
        final String secondListRes = FileSystemRepl.eval(list, fs);

        final String deleteFruitsApplesRes = FileSystemRepl.eval(deleteFruitsApples, fs);
        final String deleteFoodsFruitsApplesRes = FileSystemRepl.eval(deleteFoodsFruitsApples, fs);
        final String thirdListRes = FileSystemRepl.eval(list, fs);

        // then
        Assertions.assertEquals(createFruits, createFruitsRes);
        Assertions.assertEquals(createVegetables, createVegetablesRes);
        Assertions.assertEquals(createGrains, createGrainsRes);
        Assertions.assertEquals(createApples, createApplesRes);
        Assertions.assertEquals(createFuji, createFujiRes);
        String firstListExpected = """
                LIST
                fruits
                  apples
                    fuji
                grains
                vegetables
                """;
        Assertions.assertEquals(firstListExpected, firstListRes);

        Assertions.assertEquals(createSquash, createSquashRes);
        Assertions.assertEquals(moveSquashVegetables, moveSquashVegetablesRes);
        Assertions.assertEquals(createFoods, createFoodsRes);
        Assertions.assertEquals(moveGrainsFoods, moveGrainsFoodsRes);
        Assertions.assertEquals(moveFruitsFoods, moveFruitsFoodsRes);
        Assertions.assertEquals(createVegetablesFoods, createVegetablesFoodsRes);
        String secondListExpected = """
                LIST
                foods
                  fruits
                    apples
                      fuji
                  grains
                  vegetables
                    squash
                """;
        Assertions.assertEquals(secondListExpected, secondListRes);
        final String deleteFruitsApplesExpected = "Cannot delete fruits/apples - fruits does not exist";

        Assertions.assertEquals(deleteFruitsApplesExpected, deleteFruitsApplesRes);
        Assertions.assertEquals(deleteFoodsFruitsApples, deleteFoodsFruitsApplesRes);
        String thirdListExpected = """
                LIST
                foods
                  fruits
                  grains
                  vegetables
                    squash
                """;
        Assertions.assertEquals(thirdListExpected, thirdListRes);
    }

}
