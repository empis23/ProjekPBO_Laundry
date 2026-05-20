/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author pf344
 */
public class Admin extends User {
    public Admin() {
    }

    public Admin(int id, String username, String password) {
        super(id, username, password);
    }

    @Override
    public String getRole() {
        return "Admin";
    }

    @Override
    public String getInfo() {
        return "Admin: " + getUsername();
    }
}
