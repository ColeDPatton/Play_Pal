package com.example.application.file;

import java.io.Serializable;

/*
 * /*
 * This class converts the objects into a stream of bytes to store the object or transmit it to memory, 
 * a database, or a file.
 */

@SuppressWarnings("serial")
public class FileCompositeId implements Serializable {
    private long currentUser;
    private String game;
}