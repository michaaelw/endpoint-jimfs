package com.endponts.jimfs;

import java.util.Objects;

/**
 * Directory and file base class.
 */
public abstract class FileSystemItem implements Comparable {

    /**
     * The item parent directory.
     */
    private Directory parent;
    /**
     * The item name.
     */
    private String name;

    /**
     * Indicates whether the item is a directory or not.
     * @return true if the item is a directory false otherwise.
     */
    public abstract boolean isDirectory();

    /**
     * Constructs a new item with a name and parent directory.
     * @param n the name of the item.
     * @param p the parent of the item.
     */
    public FileSystemItem(final String n, final Directory p) {
        this.name = n;
        this.parent = p;
    }

    /**
     * Deletes the item .
     * @return true if the deletion was successful.
     */
    public boolean delete() {
        if (parent == null)
            return false;

        return this.parent.deleteItem(this);

    }

    /**
     * Retrieves the item parent directory.
     * @return the item parent directory
     */
    public Directory getParent() {
        return parent;
    }

    /**
     * Retrieves the item name.
     * @return return the item name.
     */
    public String getName() {
        return name;
    }

    /**
     * Indicates whether the item is equal to another object.
     * @param o the other object
     * @return true if the item is equal to another object and false otherwise
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof FileSystemItem)) return false;
        FileSystemItem that = (FileSystemItem) o;
        return Objects.equals(getParent(), that.getParent())
                && Objects.equals(getName(), that.getName());
    }

    /**
     * Retrieve the unique hashcode of the item.
     * @return an integer hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
