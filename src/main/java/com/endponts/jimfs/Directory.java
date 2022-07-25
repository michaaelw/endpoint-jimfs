package com.endponts.jimfs;

import java.util.Objects;
import java.util.Optional;
import java.util.TreeSet;

public class Directory extends FileSystemItem {

    /**
     * Holds the directory descendants sorted by names.
     */
    private final TreeSet<FileSystemItem> descendants;

    /**
     * @param n the name of the directory.
     * @param p a reference to the directory parent directory
     */
    public Directory(final String n, final Directory p) {
        super(n, p);
        descendants = new TreeSet<>();
    }

    /**
     * Indicates that it is a directory.
     * @return true as it is a directory
     */
    @Override
    public boolean isDirectory() {
        return true;
    }

    /**
     * Finds and return immediate child by name. If one does not exist then
     * null is returned.
     *
     * @param name is the name of the child to find.
     * @return a FileSystemItem which in our use case is a directory.
     */
    public FileSystemItem findChild(final String name) {
        final Optional<FileSystemItem> first = descendants
                .stream()
                .filter(fileSystemItem -> {
                    return fileSystemItem.getName().equalsIgnoreCase(name);
                })
                .findFirst();

        return first.isEmpty() ? null : first.get();
    }

    /**
     * Finds a descendant of the directory at any level.
     * @param name of the descendant.
     * @return a FileSystemItem.
     */
    public FileSystemItem findDescendantByName(final String name) {
        for (FileSystemItem content : descendants) {
            final String nm = content.getName();
            if (nm.equalsIgnoreCase(name)) {
                return content;
            }

            if (content.isDirectory()) {
                final Directory currDir = ((Directory) content);
                final FileSystemItem childItem;
                childItem = currDir.findDescendantByName(name);
                if (childItem != null) {
                    return childItem;
                }
            }
        }
        return null;
    }

    /**
     * Remove/Delete an immediate descendant.
     * @param item the descendant to be removed/deleted.
     * @return a boolean indicating whether the descendant was successfully
     * removed.
     */
    public boolean deleteItem(final FileSystemItem item) {
        return descendants.remove(item);
    }

    /**
     * Add a child item to the directory.
     * @param item that's added.
     */
    public void addChildItem(final FileSystemItem item) {
        descendants.add(item);
    }

    /**
     * Get the set of descendants.
     * @return the Set of descendants.
     */
    protected TreeSet<FileSystemItem> getDescendants() {
        return descendants;
    }

    /**
     * Indicates whether some other object is equal thi this directory.
     * @param o the other object.
     * @return true if it is and false otherwise.
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Directory)) return false;
        if (!super.equals(o)) return false;
        final Directory directory = (Directory) o;
        return Objects.equals(getDescendants(), directory.getDescendants());
    }

    /**
     * A unique hashcode representing the object.
     * @return  int hashcode value.
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getDescendants());
    }

    /**
     * Indicates whether another object is less than, equal, or greater
     * than this one.
     * @param o the object to be compared.
     * @return int value of less than -1, equal 0 and greater than 1
     */
    @Override
    public int compareTo(final Object o) {
        return this.getName().compareTo(((Directory) o).getName());
    }
}
