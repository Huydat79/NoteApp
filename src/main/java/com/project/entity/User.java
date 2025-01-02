package com.project.entity;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Một transfer cho dữ liệu của các user
 */

public class User {
    private String name;
    private String username;
    private String password;
    private Date birthday;
    private String school;
    private Gender gender;
    
    /**
     * Các giới tính
     */
    public static enum Gender {
        MALE, FEMALE, OTHER;
    }

    /**
     * Constructor và cài đặt dữ liệu default cho User
     */
    public User() {
        this.name = "";
        this.username = "";
        this.password = "";
        this.birthday = Date.valueOf(LocalDate.MIN);
        this.school = "";
        this.gender = Gender.MALE;
    }

    public User(int id, String name, String username, String password, Date birthday, String school, Gender gender) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.birthday = birthday;
        this.school = school;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getSchool() {
        return school;
    }

    public Gender getGender() {
        return gender;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
    
    /**
     * Kiểm tra xem một thể hiện User có mang giá trị default không
     * @return (1) {@code true} nếu đây là default User, (2) {@code false} nếu ngược lại
     */
    public boolean isDefaultValue() {
        return "".equals(username);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.name);
        hash = 47 * hash + Objects.hashCode(this.username);
        hash = 47 * hash + Objects.hashCode(this.password);
        hash = 47 * hash + Objects.hashCode(this.birthday);
        hash = 47 * hash + Objects.hashCode(this.school);
        hash = 47 * hash + Objects.hashCode(this.gender);
        return hash;
    }

    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        if (!Objects.equals(this.password, other.password)) {
            return false;
        }
        if (!Objects.equals(this.school, other.school)) {
            return false;
        }
        if (!Objects.equals(this.birthday, other.birthday)) {
            return false;
        }
        return this.gender == other.gender;
    }



}