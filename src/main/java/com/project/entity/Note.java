package com.project.entity;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Một transfer cho dữ liệu của các note
 */
public class Note {
    private int id;
    private String author;
    private String header;
    private String content;
    private int lastModified;
    private Date lastModifiedDate;
    private List<NoteFilter> filters;
  
    /**
     * Constructor và cài đặt dữ liệu default cho Note
     */
    public Note() {
        this.id = -1;
        this.author = "";
        this.header = "";
        this.content = "";
        this.lastModified = 0;
        this.lastModifiedDate = Date.valueOf(LocalDate.MIN);
        this.filters = new ArrayList<>();
    }

    public Note(int id, String author, String header, String content, int lastModified, Date lastModifiedDate, List<NoteFilter> filters) {
        this.id = id;
        this.author = author;
        this.header = header;
        this.content = content;
        this.lastModified = lastModified;
        this.lastModifiedDate = lastModifiedDate;
        this.filters = filters;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
    
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLastModified() {
        return lastModified;
    }

    public void setLastModified(int lastModified) {
        this.lastModified = lastModified;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public List<NoteFilter> getFilters() {
        return filters;
    }

    public void setFilters(List<NoteFilter> filters) {
        this.filters = filters;
    }
    
    /**
     * Kiểm tra xem một thể hiện Note có mang giá trị default không
     * @return (1) {@code true} nếu đây là default Note, (2) {@code false} nếu ngược lại
     */
    public boolean isDefaultValue() {
        return id == -1;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + this.id;
        hash = 73 * hash + Objects.hashCode(this.author);
        hash = 73 * hash + Objects.hashCode(this.header);
        hash = 73 * hash + this.lastModified;
        hash = 73 * hash + Objects.hashCode(this.lastModifiedDate);
        hash = 73 * hash + Objects.hashCode(this.filters);
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
        final Note other = (Note) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.lastModified != other.lastModified) {
            return false;
        }
        if (!Objects.equals(this.author, other.author)) {
            return false;
        }
        if (!Objects.equals(this.content, other.content)) {
            return false;
        }
        if (!Objects.equals(this.header, other.header)) {
            return false;
        }
        if (!Objects.equals(this.lastModifiedDate, other.lastModifiedDate)) {
            return false;
        }
        return Objects.equals(this.filters, other.filters);
    }
     
}
