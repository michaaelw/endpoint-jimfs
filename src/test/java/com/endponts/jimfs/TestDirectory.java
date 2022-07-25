package com.endponts.jimfs;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestDirectory {

    //////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////Root Directory////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////

    @Test
    @SuppressWarnings("ConstantConditions")
    public void testRootDirectory() {
        // given
        final String name = "/";
        final Directory parent = null;

        // when
        final Directory directory = new Directory(name, parent);

        // then
        Assertions.assertEquals(name, directory.getName());
        Assertions.assertEquals(parent, directory.getParent());
        Assertions.assertTrue(directory.isDirectory());
    }

    //////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////Find Child Directory//////////////////////////
    //////////////////////////////////////////////////////////////////////////////

    @Test
    @SuppressWarnings("ConstantConditions")
    public void testFindChildWithNoChildDirectory() {
        // given
        final String name = "/";
        final Directory parent = null;
        final String childDirFruits = "fruits";

        // when
        final Directory directory = new Directory(name, parent);
        final Directory childDirectory = (Directory)directory.findChild(childDirFruits);

        // then
        Assertions.assertEquals(name, directory.getName());
        Assertions.assertEquals(parent, directory.getParent());
        Assertions.assertTrue(directory.isDirectory());
        Assertions.assertNull(childDirectory);
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void testFindChildWithChildDirectory() {
        // given
        final String name = "/";
        final Directory parent = null;
        final String childDirFruits = "fruits";

        // when
        final Directory rootDir = new Directory(name, parent);
        final Directory fruitsDir = new Directory(childDirFruits, rootDir);
        rootDir.addChildItem(fruitsDir);
        final Directory foundDirectory = (Directory)rootDir.findChild(childDirFruits);

        // then
        Assertions.assertEquals(name, rootDir.getName());
        Assertions.assertEquals(parent, rootDir.getParent());
        Assertions.assertTrue(rootDir.isDirectory());
        Assertions.assertNotNull(foundDirectory);
        Assertions.assertEquals(fruitsDir.getName(), foundDirectory.getName());
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void testFindChildDeepDirectoryTwoDeep() {
        // given
        final String name = "/";
        final Directory parent = null;
        final String childDirFruits = "fruits";
        final String childDirApples = "apples";

        // when
        final Directory rootDir = new Directory(name, parent);
        final Directory fruitsDir = new Directory(childDirFruits, rootDir);
        rootDir.addChildItem(fruitsDir);
        final Directory applesDir = new Directory(childDirApples, fruitsDir);
        fruitsDir.addChildItem(applesDir);
        final Directory foundDirectory = (Directory)rootDir.findDescendantByName(childDirApples);

        // then
        Assertions.assertEquals(name, rootDir.getName());
        Assertions.assertEquals(parent, rootDir.getParent());
        Assertions.assertTrue(rootDir.isDirectory());
        Assertions.assertNotNull(foundDirectory);
        Assertions.assertEquals(applesDir.getName(), foundDirectory.getName());
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void testAddItemDirectory() {
        // given
        final String name = "/";
        final Directory parent = null;
        final String childDirFruits = "fruits";
        final String childDirApples = "apples";

        // when
        final Directory rootDir = new Directory(name, parent);
        final Directory fruitsDir = new Directory(childDirFruits, rootDir);
        rootDir.addChildItem(fruitsDir);
        final Directory applesDir = new Directory(childDirApples, fruitsDir);
        fruitsDir.addChildItem(applesDir);
        final Directory foundDirectory = (Directory)rootDir.findDescendantByName(childDirApples);

        // then
        Assertions.assertEquals(name, rootDir.getName());
        Assertions.assertEquals(parent, rootDir.getParent());
        Assertions.assertTrue(rootDir.isDirectory());
        Assertions.assertNotNull(foundDirectory);
        Assertions.assertEquals(applesDir.getName(), foundDirectory.getName());
        final int addCount =  1;
        Assertions.assertEquals(addCount, rootDir.getDescendants().size());
        Assertions.assertEquals(addCount, fruitsDir.getDescendants().size());
        Assertions.assertEquals(0, applesDir.getDescendants().size());
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void testDeleteDirectory() {
        // given
        final String name = "/";
        final Directory parent = null;
        final String childDirFruits = "fruits";
        final String childDirApples = "apples";

        // when
        final Directory rootDir = new Directory(name, parent);
        final Directory fruitsDir = new Directory(childDirFruits, rootDir);
        rootDir.addChildItem(fruitsDir);
        final Directory applesDir = new Directory(childDirApples, fruitsDir);
        fruitsDir.addChildItem(applesDir);
        final Directory foundDirectory = (Directory)rootDir.findDescendantByName(childDirApples);

        // then
        Assertions.assertEquals(name, rootDir.getName());
        Assertions.assertEquals(parent, rootDir.getParent());
        Assertions.assertTrue(rootDir.isDirectory());
        Assertions.assertNotNull(foundDirectory);
        Assertions.assertEquals(applesDir.getName(), foundDirectory.getName());
        final int oneChild =  1;
        final int zeroChild =  0;
        Assertions.assertEquals(oneChild, rootDir.getDescendants().size());
        Assertions.assertEquals(oneChild, fruitsDir.getDescendants().size());
        Assertions.assertEquals(zeroChild, applesDir.getDescendants().size());
        fruitsDir.deleteItem(applesDir);
        Assertions.assertEquals(zeroChild, fruitsDir.getDescendants().size());
        fruitsDir.delete();
        Assertions.assertEquals(zeroChild, rootDir.getDescendants().size());
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void testEqualDirectory() {
        // given
        final String name = "/";
        final Directory parent = null;
        final String childDirFruits = "fruits";
        final String childDirApples = "apples";
        final String childDirBadname = "badname";

        // when
        final Directory rootDir = new Directory(name, parent);
        final Directory fruitsDir = new Directory(childDirFruits, rootDir);
        rootDir.addChildItem(fruitsDir);
        final Directory applesDir = new Directory(childDirApples, fruitsDir);
        fruitsDir.addChildItem(applesDir);
        final Directory foundDirectory = (Directory)rootDir.findDescendantByName(childDirApples);
        final Directory foundDirBadname = (Directory)rootDir.findDescendantByName(childDirBadname);

        // then
        Assertions.assertEquals(name, rootDir.getName());
        Assertions.assertEquals(parent, rootDir.getParent());
        Assertions.assertTrue(rootDir.isDirectory());
        Assertions.assertNotNull(foundDirectory);
        Assertions.assertEquals(applesDir.getName(), foundDirectory.getName());
        Assertions.assertTrue(rootDir.equals(rootDir));
        Assertions.assertEquals(rootDir, rootDir);
        Assertions.assertNotEquals(rootDir, fruitsDir);
        Assertions.assertFalse(rootDir.equals(fruitsDir));
        Assertions.assertEquals(rootDir.hashCode(), rootDir.hashCode());
        Assertions.assertNotEquals(rootDir.hashCode(), fruitsDir.hashCode());
        Assertions.assertNull(foundDirBadname);
        final int oneChild =  1;
        final int zeroChild =  0;
        Assertions.assertEquals(oneChild, rootDir.getDescendants().size());
        Assertions.assertEquals(oneChild, fruitsDir.getDescendants().size());
        Assertions.assertEquals(zeroChild, applesDir.getDescendants().size());
        fruitsDir.deleteItem(applesDir);
        Assertions.assertEquals(zeroChild, fruitsDir.getDescendants().size());
        fruitsDir.delete();
        Assertions.assertEquals(zeroChild, rootDir.getDescendants().size());

    }

}
