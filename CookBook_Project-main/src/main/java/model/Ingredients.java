package model;

import java.io.Serializable;
/**
 * Represents a ingredients entity.
 * Each ingredients entry contains information such as id and name.
 * Maps to the 'ingredients' table in the database.
 * 
 * @author: Pan Zitao
 */

public class Ingredients implements Serializable {
    
    private int id;
    private String name;

    /**
     * Default constructor for the Ingredients class.
     */
    public Ingredients() {

    }

    /**
     * Constructs an Ingredients object with all fields.
     * @param id The ID of the ingredient (primary key).
     * @param name The name of the ingredient.
     */
    public Ingredients(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;  
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Ingredients{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}